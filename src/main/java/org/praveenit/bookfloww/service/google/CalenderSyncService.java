package org.praveenit.bookfloww.service.google;

import org.praveenit.bookfloww.dto.google.GoogleEventsResponseDto;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.RefreshTokenRepository;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.praveenit.bookfloww.service.EncryptionService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalenderSyncService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final EncryptionService encryptionService;
	private final GoogleTokenService googleTokenService;
	private final GoogleCalenderService googleCalenderService;

	public GoogleEventsResponseDto fetchUserCalender(User user) {
		RefreshTokenEntity token = refreshTokenRepository
				.findTopByUserAndStatusOrderByIdDesc(user, RefreshTokenEntity.TokenStatus.ACTIVE)
				.orElseThrow(() -> new TokenException(TokenError.NO_ACTIVE_REFRESH_TOKEN));
		
		if (token.getGoogleRefreshTokenEnc() == null) {
			throw new TokenException(TokenError.GOOGLE_NOT_LINKED);
		}
		
		String googleRefreshToken =
				encryptionService.decrypt(token.getGoogleRefreshTokenEnc());
		
		String googleAccessToken = 
				googleTokenService.refreshAccessToken(googleRefreshToken);
		
		return googleCalenderService.fetchEvents(googleAccessToken);

	}
	public String getGoogleAccessToken(User user) {
		RefreshTokenEntity token=refreshTokenRepository.findTopByUserAndStatusOrderByIdDesc(
				user, 
				RefreshTokenEntity.TokenStatus.ACTIVE)
		.orElseThrow(()->new TokenException(TokenError.NO_ACTIVE_REFRESH_TOKEN));
		
		if(token.getGoogleRefreshTokenEnc()==null) {
			throw new TokenException(TokenError.GOOGLE_NOT_LINKED);
		}
		
		String googleRefreshToken=
				encryptionService.decrypt(token.getGoogleRefreshTokenEnc());
		
		return googleTokenService.refreshAccessToken(googleRefreshToken);
	}

}
