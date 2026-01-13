package org.praveenit.bookfloww.security.impl;

public class BookingException extends RuntimeException {
	
	private final BookingError error;

	public BookingException(BookingError error) {
		super();
		this.error = error;
	}

	public BookingError getError() {
		return error;
	}
	
	

}
