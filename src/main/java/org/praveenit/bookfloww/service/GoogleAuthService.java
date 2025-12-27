package org.praveenit.bookfloww.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
	
	private final RestTemplate restTemplate;
	
	@Value("${google.client.id}")
	private String clientId;

	@Value("${google.client.secret}")
	private String clientSecret;

	@Value("${google.redirect.uri}")
	private String redirectUri;

	@Value("${google.token.uri}")
	private String tokenUri;

	@Value("${google.userinfo.uri}")
	private String userInfoUri;

	// Exchange authorization code for access token
	public Map<String, Object> exchangeCodeForTokens(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// MultiValueMap for form data
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		body.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		Map<String, Object> response = restTemplate.postForObject(tokenUri, request, Map.class);
		return response;
	}

	// fetch user profile from google
	public Map<String, Object> getUserProfile(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);// set authorization header
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> response=restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
		return response.getBody();
	}
	
	// Build Google login URL
	public String buildGoogleLoginUrl() {

	    StringBuilder url = new StringBuilder("https://accounts.google.com/o/oauth2/v2/auth");
	    url.append("?client_id=").append(clientId)
	       .append("&redirect_uri=").append(redirectUri)
	       .append("&response_type=code")
	       .append("&scope=openid%20email%20profile")
	       .append("&access_type=offline")
	       .append("&prompt=consent");

	    return url.toString();
	}


}
