package org.praveenit.bookfloww.security.impl;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {

    private final TokenError tokenError;

    public TokenException(TokenError tokenError) {
        super(tokenError.getDescription());
        this.tokenError = tokenError;
    }
}
