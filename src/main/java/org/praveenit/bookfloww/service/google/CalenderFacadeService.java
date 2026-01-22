package org.praveenit.bookfloww.service.google;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.praveenit.bookfloww.dto.google.GoogleEventsResponseDto;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.RefreshTokenRepository;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.praveenit.bookfloww.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalenderFacadeService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final RestTemplate restTemplate;
	private final GoogleTokenService googleTokenService;
	private final EncryptionService encryptionService;
	private final SecurityUtil securityUtil;
	
	@Value("${google.calendar.events.url}")
	private String googleEventsUrl;

	public GoogleEventsResponseDto fetchThreeMonthEvents() {
		// Get logged-in user
		User user = securityUtil.getCurrentUser();

		// Get Google refresh token
		RefreshTokenEntity token = refreshTokenRepository
				.findTopByUserAndStatusOrderByIdDesc(user, RefreshTokenEntity.TokenStatus.ACTIVE)
				.orElseThrow(() -> new TokenException(TokenError.NO_ACTIVE_REFRESH_TOKEN));

		if (token.getGoogleRefreshTokenEnc() == null) {
			throw new TokenException(TokenError.GOOGLE_NOT_LINKED);
		}

		// decrypt google token
		String googleRefreshToken = encryptionService.decrypt(token.getGoogleRefreshTokenEnc());

		// Refresh access token
		String accessToken = googleTokenService.refreshAccessToken(googleRefreshToken);

		// Calculate date range
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		String timeMin = now.minusMonths(1).with(LocalTime.MIN).toInstant().toString();
		String timeMax = now.plusMonths(1).with(LocalTime.MAX).toInstant().toString();

		// Call Google API
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		
		String url = UriComponentsBuilder.fromHttpUrl(googleEventsUrl).queryParam("timeMin", timeMin)
				.queryParam("timeMax", timeMax).queryParam("singleEvents", true).queryParam("orderBy", "startTime")
				.toUriString();

		try {
			ResponseEntity<GoogleEventsResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					GoogleEventsResponseDto.class);
			log.info("fetched from booking");
			return response.getBody();
		} catch (RestClientException ex) {
			log.error("Failed to fetch from bookings", ex);
			throw ex;
		}
	}

}
