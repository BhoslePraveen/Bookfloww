package org.praveenit.bookfloww.service;

import java.util.Map;
import org.praveenit.bookfloww.dto.TokenResponse;
import org.praveenit.bookfloww.entity.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final OAuthService oAuthService;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse handleGoogleCallback(String code) {

        // 1. Exchange code for Google tokens
        Map<String, Object> tokens =
                googleAuthService.exchangeCodeForTokens(code);

        String googleAccessToken =
                (String) tokens.get("access_token");

        String googleRefreshToken =
                (String) tokens.get("refresh_token");

        // 2. Fetch profile
        Map<String, Object> profile =
                googleAuthService.getUserProfile(googleAccessToken);

        String email = (String) profile.get("email");
        String name = (String) profile.get("name");

        // 3. Find or create user
        User user = userService.findOrCreateGoogleUser(email, name);

        // 4. Issue APP tokens (delegate)
        TokenResponse response = oAuthService.issueTokens(user);

        // 5. Store Google refresh token (optional)
        refreshTokenService.createRefreshToken(
                user, googleRefreshToken);

        return response;
    }
}
