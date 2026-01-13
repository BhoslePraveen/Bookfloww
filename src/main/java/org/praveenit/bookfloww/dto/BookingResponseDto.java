package org.praveenit.bookfloww.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponseDto {
	    private Long id;
	    private Instant startTime;
	    private Instant endTime;
	    private String status;
	    private String source;
	    private String googleEventId;
	    private String customerName;
	    private String customerEmail;
	    private String meetingDescription;
	    private String meetLink;
	    

}
