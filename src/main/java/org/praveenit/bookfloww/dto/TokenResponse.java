package org.praveenit.bookfloww.dto;

import java.time.Instant;
import lombok.Getter;

@Getter
public class TokenResponse {

    private final boolean success = true;
    private final TokenData data;
    private final Instant timestamp = Instant.now();

    public TokenResponse(TokenData data) {
        this.data = data;
    }
}
