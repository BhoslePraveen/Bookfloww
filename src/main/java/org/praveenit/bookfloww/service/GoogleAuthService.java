package org.praveenit.bookfloww.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.dto.auth.GoogleTokenResponse;
import org.praveenit.bookfloww.dto.auth.GoogleUserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {
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

    private final RestTemplate restTemplate;

    // Exchange authorization code for access token
    public GoogleTokenResponse exchangeCodeForTokens(String code) {
        log.info("Exchanging authorization code with Google");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<GoogleTokenResponse> response =
                    restTemplate.exchange(
                            tokenUri,
                            HttpMethod.POST,
                            request,
                            GoogleTokenResponse.class
                    );

            log.info("Google token exchange successful");
            return response.getBody();

        } catch (Exception ex) {
            log.error("Google token exchange failed", ex);
            throw ex;
        }
    }

    /**
     * Fetch Google user profile using access token
     */
    public GoogleUserProfile getUserProfile(String accessToken) {
        log.info("Fetching Google user profile");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleUserProfile> response =
                    restTemplate.exchange(
                            userInfoUri,
                            HttpMethod.GET,
                            entity,
                            GoogleUserProfile.class
                    );

            log.info("Google user profile fetched successfully");
            return response.getBody();

        } catch (RestClientException ex) {
            log.error("Failed to fetch Google user profile", ex);
            throw ex;
        }
    }

    /**
     * Build Google login URL
     */
    public String buildGoogleLoginUrl() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
    }
}
