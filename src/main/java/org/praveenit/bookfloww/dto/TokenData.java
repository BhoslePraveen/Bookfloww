package org.praveenit.bookfloww.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class TokenData {

    private static final String TOKEN_TYPE = "Bearer";
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresIn;
    private Instant accessTokenExpiresAt;
    private long refreshTokenExpiresIn;
    private Instant refreshTokenExpiresAt;

    public String getTokenType() {
        return TOKEN_TYPE;
    }
}
