package org.praveenit.bookfloww.service;

import java.time.Instant;

import org.praveenit.bookfloww.dto.TokenData;
import org.praveenit.bookfloww.dto.TokenResponse;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class OAuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${jwt.access.expiration}")
    private long accessTokenDurationMs;

    public TokenResponse issueTokens(User user) {

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        TokenData tokenData = new TokenData(
                accessToken,
                refreshToken,
                accessTokenDurationMs / 1000,
                Instant.now().plusMillis(accessTokenDurationMs),
                0,
                null
        );

        return new TokenResponse(tokenData);
    }

    public TokenResponse refreshTokens(RefreshTokenEntity storedToken) {

        String newAccessToken =
                jwtUtil.generateAccessToken(storedToken.getUser());

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(storedToken);

        TokenData tokenData = new TokenData(
                newAccessToken,
                newRefreshToken,
                accessTokenDurationMs / 1000,
                Instant.now().plusMillis(accessTokenDurationMs),
                storedToken.getExpiryDate().getEpochSecond()
                        - Instant.now().getEpochSecond(),
                storedToken.getExpiryDate()
        );

        return new TokenResponse(tokenData);
    }
}

