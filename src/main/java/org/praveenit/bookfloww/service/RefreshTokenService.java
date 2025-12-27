package org.praveenit.bookfloww.service;

import java.time.Instant;
import java.util.UUID;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.RefreshTokenEntity.TokenStatus;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.RefreshTokenRepository;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final HashService hashService;
	private final EncryptionService encryptionService;

	// 7 days refresh token validity
	// private final long refreshTokenDurationMs = 7 * 24 * 60 * 60 * 1000;
	@Value("${jwt.refresh.expiration}")
	private long refreshTokenDurationMs;

	// ✅ NORMAL LOGIN / LOCAL / GENERIC
	public String createRefreshToken(User user) {
		return createRefreshTokenInternal(user, null);
	}

	// ✅ GOOGLE OAUTH LOGIN
	public String createRefreshToken(User user, String googleRefreshToken) {
		return createRefreshTokenInternal(user, googleRefreshToken);
	}

	// Create Refresh Token (Login)
	public String createRefreshTokenInternal(User user, String googleRefreshToken) {
		String rawToken = UUID.randomUUID().toString();
		String hashedToken = hashService.hash(rawToken); // ✅ HASH RAW TOKEN
		
		RefreshTokenEntity token = new RefreshTokenEntity();
		token.setUser(user);
		token.setTokenHash(hashedToken);
		token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		token.setStatus(RefreshTokenEntity.TokenStatus.ACTIVE);

		// store google refresh token only if present
		if (googleRefreshToken != null && !googleRefreshToken.isBlank()) {
			token.setGoogleRefreshTokenEnc(encryptionService.encrypt(googleRefreshToken));
			
		}
		refreshTokenRepository.save(token);
		// ⚠️ Return RAW token ONLY ONCE
        return rawToken;
	}

	// VERIFY
	public RefreshTokenEntity verifyRefreshToken(String rawToken) {

		return refreshTokenRepository
		        .findAllByStatus(TokenStatus.ACTIVE)
		        .stream()
		        .filter(token ->
		            hashService.matches(rawToken, token.getTokenHash())
		        )
		        .findFirst()
		        .map(token -> {
		            if (token.getExpiryDate().isBefore(Instant.now())) {
		                token.setStatus(TokenStatus.INACTIVE);
		                refreshTokenRepository.save(token);
		                throw new TokenException(TokenError.REFRESH_TOKEN_EXPIRED);
		            }
		            return token;
		        })
		        .orElseThrow(() ->
		            new TokenException(TokenError.INVALID_GRANT)
		        );
	}

	// ROTATE
	@Transactional
	public String rotateRefreshToken(RefreshTokenEntity oldToken) {
		// Soft-delete old token
		oldToken.setStatus(TokenStatus.INACTIVE);
		refreshTokenRepository.save(oldToken);
		// decrypt Google refresh token before reusing
	    String googleRefreshToken = null;
	    if (oldToken.getGoogleRefreshTokenEnc() != null) {
	        googleRefreshToken =
	            encryptionService.decrypt(oldToken.getGoogleRefreshTokenEnc());
	    }
		// Create new APP refresh token with same Google token
		return createRefreshToken(oldToken.getUser(), googleRefreshToken);
	}
}
