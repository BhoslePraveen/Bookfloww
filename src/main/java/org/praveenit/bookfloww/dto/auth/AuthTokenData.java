package org.praveenit.bookfloww.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenData {
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessToken {
        private String value;
//        private String tokenType;
        private long expiresIn;      // seconds
        private Instant expiresAt;
        public String getTokenType() {
            return "Bearer";
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshToken {
        private String value;
        private long expiresIn;      // seconds
        private Instant expiresAt;   // nullable
    }
}
