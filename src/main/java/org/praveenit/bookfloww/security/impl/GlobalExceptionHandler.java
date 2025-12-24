package org.praveenit.bookfloww.security.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(TokenException.class)
	public ResponseEntity<ErrorResponse> handleTokenException(TokenException ex) {
		TokenError error = ex.getTokenError();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse(error.getCode(), error.getDescription(), error.getAction()));
	}

}
