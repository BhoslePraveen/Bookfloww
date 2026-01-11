package org.praveenit.bookfloww.security.impl;

public class TokenException extends RuntimeException {
	private final TokenError tokenError;

	public TokenException(TokenError tokenError) {
		super(tokenError.getDescription());
		this.tokenError = tokenError;
	}

	public TokenError getTokenError() {
		return tokenError;
	}

}
