package org.praveenit.bookfloww.dto;

import java.time.Instant;

public class TokenResponse {

    private boolean success = true;
    private TokenData data;
    private Instant timestamp = Instant.now();

    public TokenResponse(TokenData data) {
        this.data = data;
    }

    // Jackson getters
    public boolean isSuccess() { return success; }
    public TokenData getData() { return data; }
    public Instant getTimestamp() { return timestamp; }
}


