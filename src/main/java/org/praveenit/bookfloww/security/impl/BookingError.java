package org.praveenit.bookfloww.security.impl;

public enum BookingError {
	
	SLOT_ALREADY_BOOKED(
			"slot_already_booked",
			"Time slot already booked",
			"CHOOSE_DIFFERENT_TIME"
			),
	
	BOOKING_NOT_FOUND(
			"booking_not_found",
			"Booking not found",
			"REFRESH_PAGE"
			),
	
	GOOGLE_BOOKING_READ_ONLY(
			"google_booking_read_only",
			"Google bookings cannot be updated manually",
			"EDIT_IN_GOOGLE"
			);
	
	private final String code;
	private final String description;
	private final String action;
	
	

	private BookingError(String code, String description, String action) {
		this.code = code;
		this.description = description;
		this.action = action;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getAction() {
		return action;
	}

	
	
	
	
	
	

}
