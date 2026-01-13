package org.praveenit.bookfloww.service.google;

import org.praveenit.bookfloww.dto.auth.GoogleTokenResponse;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenService {

	@Value("${google.client.id}")
	private String clientId;

	@Value("${google.client.secret}")
	private String clientSecret;
	
	@Value("${google.api.token}")
	private String googleTokenUrl;

	private final RestTemplate restTemplate;

	public String refreshAccessToken(String googleRefreshToken) {
		log.info("Refreshing gooogle access token using google refresh token");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("refresh_token", googleRefreshToken);
		body.add("grant_type", "refresh_token");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		try {
			ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
					googleTokenUrl, 
					request, 
					GoogleTokenResponse.class);
			GoogleTokenResponse tokenResponse = response.getBody();
			if (tokenResponse == null || tokenResponse.getAccess_token() ==null) {
				throw new TokenException(TokenError.GOOGLE_TOKEN_FAILED);
			}
			
			return tokenResponse.getAccess_token();
			
		} catch (HttpClientErrorException ex) {

			// Google sends OAuth errors here
			if (ex.getResponseBodyAsString().contains("invalid_grant")) {
				throw new TokenException(TokenError.INVALID_GRANT);
			}

			throw new TokenException(TokenError.GOOGLE_TOKEN_FAILED);
		}
	}
}
