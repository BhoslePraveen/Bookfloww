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

    USER_NOT_FOUND(
		    "user_not_found",
		    "User does not exist or account is inactive",
		    "RE_LOGIN"
		    ),
    
    INVALID_GRANT(
        "invalid_grant",
        "Invalid refresh token",
        "RE_LOGIN"
    ),
    
    GOOGLE_TOKEN_FAILED(
	        "google_token_failed",
	        "Unable to refresh Google access token",
	        "RE_LOGIN"
	        ),

	GOOGLE_NOT_LINKED(
			"google_not_linked", 
			"Google account is not linked. Please connect Google Calendar.",
			"CONNECT_GOOGLE"
			),

	NO_ACTIVE_REFRESH_TOKEN(
			"no_active_refresh_token", 
			"No active session found. Please login again.", 
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

