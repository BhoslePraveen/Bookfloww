package org.praveenit.bookfloww.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.RefreshTokenEntity.TokenStatus;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.RefreshTokenRepository;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final HashService hashService;
    private final EncryptionService encryptionService;
    private final RefreshTokenRepository refreshTokenRepository;

    // ✅ NORMAL LOGIN / LOCAL / GENERIC
    public String createRefreshToken(User user) {
        return createRefreshTokenInternal(user, null);
    }

    // ✅ GOOGLE OAUTH LOGIN
    public String createRefreshToken(User user, String googleRefreshToken) {
        return createRefreshTokenInternal(user, googleRefreshToken);
    }

    // Create Refresh Token at Login
    public String createRefreshTokenInternal(User user, String googleRefreshToken) {
        String rawToken = UUID.randomUUID().toString();
        String tokenId = UUID.randomUUID().toString(); // lookup key
        String hashedToken = hashService.hash(rawToken);

        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setUser(user);
        token.setTokenId(tokenId);
        token.setTokenHash(hashedToken);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setStatus(RefreshTokenEntity.TokenStatus.ACTIVE);

        // store google refresh token only if present
        if (googleRefreshToken != null && !googleRefreshToken.isBlank()) {
            token.setGoogleRefreshTokenEnc(encryptionService.encrypt(googleRefreshToken));

        }
        refreshTokenRepository.save(token);

        // return tokenId.rawToken (composite token)
        return tokenId + "." + rawToken;
    }

    public RefreshTokenEntity verifyRefreshToken(String presentedToken) {
        String[] parts = presentedToken.split("\\.");
        if (parts.length != 2) {
            throw new TokenException(TokenError.INVALID_GRANT);
        }

        String tokenId = parts[0];
        String rawToken = parts[1];

        RefreshTokenEntity token =
                refreshTokenRepository
                        .findByTokenId(tokenId)
                        .orElseThrow(() ->
                                new TokenException(TokenError.INVALID_GRANT)
                        );

        if (!hashService.matches(rawToken, token.getTokenHash())) {
            throw new TokenException(TokenError.INVALID_GRANT);
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            token.setStatus(TokenStatus.INACTIVE);
            refreshTokenRepository.save(token);
            throw new TokenException(TokenError.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

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
