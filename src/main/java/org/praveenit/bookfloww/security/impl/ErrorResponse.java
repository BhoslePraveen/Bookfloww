package org.praveenit.bookfloww.security.impl;

public class ErrorResponse {
	private String error;
	private String errorDescription;
	private String action;

	public ErrorResponse(String error, String errorDescription, String action) {
		this.error = error;
		this.errorDescription = errorDescription;
		this.action = action;
	}

	public String getError() {
		return error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getAction() {
		return action;
	}

}
