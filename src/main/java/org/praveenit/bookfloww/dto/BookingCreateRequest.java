package org.praveenit.bookfloww.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCreateRequest {
	
	private Instant start;
    private Instant end;

    private String customerName;
    private String customerEmail;
    private String meetingDescription;

}
