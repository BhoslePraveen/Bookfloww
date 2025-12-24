package org.praveenit.bookfloww.dto;

import java.time.Instant;

public class TokenData {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long accessTokenExpiresIn;
    private Instant accessTokenExpiresAt;
    private long refreshTokenExpiresIn;
    private Instant refreshTokenExpiresAt;

    public TokenData(String accessToken, String refreshToken,
                     long accessTokenExpiresIn, Instant accessTokenExpiresAt,
                     long refreshTokenExpiresIn, Instant refreshTokenExpiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    // Jackson getters
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getTokenType() { return tokenType; }
    public long getAccessTokenExpiresIn() { return accessTokenExpiresIn; }
    public Instant getAccessTokenExpiresAt() { return accessTokenExpiresAt; }
    public long getRefreshTokenExpiresIn() { return refreshTokenExpiresIn; }
    public Instant getRefreshTokenExpiresAt() { return refreshTokenExpiresAt; }
}
