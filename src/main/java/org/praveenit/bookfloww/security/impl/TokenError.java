package org.praveenit.bookfloww.security.impl;

public enum TokenError {

    ACCESS_TOKEN_EXPIRED(
        "access_token_expired",
        "Access token has expired",
        "REFRESH"
    ),

    REFRESH_TOKEN_EXPIRED(
        "refresh_token_expired",
        "Refresh token has expired. Please login again.",
        "RE_LOGIN"
    ),

    INVALID_GRANT(
        "invalid_grant",
        "Invalid refresh token",
        "RE_LOGIN"
    );

    private final String code;
    private final String description;
    private final String action;

    TokenError(String code, String description, String action) {
        this.code = code;
        this.description = description;
        this.action = action;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getAction() { return action; }
}

