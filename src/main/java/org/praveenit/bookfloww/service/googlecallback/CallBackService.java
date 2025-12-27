package org.praveenit.bookfloww.service.googlecallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.dto.auth.AuthTokenData;
import org.praveenit.bookfloww.dto.auth.GoogleTokenResponse;
import org.praveenit.bookfloww.dto.auth.GoogleUserProfile;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.security.JwtUtil;
import org.praveenit.bookfloww.service.GoogleAuthService;
import org.praveenit.bookfloww.service.RefreshTokenService;
import org.praveenit.bookfloww.service.UserService;
import org.praveenit.bookfloww.utils.SensitiveData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallBackService {
    @Value("${jwt.access.expiration}")
    private long accessTokenDurationMs;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final GoogleAuthService googleAuthService;
    private final RefreshTokenService refreshTokenService;

    public AuthTokenData processCallBack(String code) {
        log.info("Google OAuth callback processing started");

        // 1. Exchange authorization code for Google tokens
        log.debug("Exchanging authorization code with Google");
        GoogleTokenResponse googleTokenResponse = googleAuthService.exchangeCodeForTokens(code);
        String googleAccessToken = googleTokenResponse.getAccess_token();
        String googleRefreshToken = googleTokenResponse.getRefresh_token();

        if (googleAccessToken == null) {
            log.error("Google access token not received");
            throw new IllegalStateException("Google access token missing");
        }

        log.debug("Google token exchange successful");

        // 2. Fetch user profile
        log.debug("Fetching Google user profile");
        GoogleUserProfile profile = googleAuthService.getUserProfile(googleAccessToken);
        String email = profile.getEmail();
        String name = profile.getName();

        if (email == null) {
            log.error("Email not received from Google profile");
            throw new IllegalStateException("Google profile email missing");
        }

        log.info("Google user profile fetched for email={}", SensitiveData.maskEmail(email));

        // 3. Find or create user
        User user = userService.findOrCreateGoogleUser(email, name);
        log.info("User resolved successfully. userId={}", user.getId());

        // 4. Generate application JWT access token
        String jwtAccessToken = jwtUtil.generateAccessToken(user);
        log.info("JWT access token generated successfully");

        // 5. Create a refresh token and persist Google refresh token
        String appRefreshToken = refreshTokenService.createRefreshToken(user, googleRefreshToken);

        // 6. Build response
        return AuthTokenData.builder()
                .accessToken(
                        new AuthTokenData.AccessToken(
                                jwtAccessToken,
                                accessTokenDurationMs / 1000,
                                Instant.now().plusMillis(accessTokenDurationMs)
                        )
                )
                .refreshToken(
                        AuthTokenData.RefreshToken.builder()
                                .value(appRefreshToken)
                                .expiresIn(refreshTokenDurationMs / 1000)
                                .expiresAt(Instant.now().plusMillis(refreshTokenDurationMs))
                                .build()
                )
                .build();
    }

}
