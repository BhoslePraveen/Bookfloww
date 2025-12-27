package org.praveenit.bookfloww.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.dto.auth.AuthTokenData;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    @Value("${jwt.access.expiration}")
    private long accessTokenDurationMs;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthTokenData refreshTokens(RefreshTokenEntity storedToken) {
        String jwtAccessToken = jwtUtil.generateAccessToken(storedToken.getUser());
        String appRefreshToken = refreshTokenService.rotateRefreshToken(storedToken);

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

