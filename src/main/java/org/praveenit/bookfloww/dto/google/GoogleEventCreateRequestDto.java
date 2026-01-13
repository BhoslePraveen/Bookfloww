package org.praveenit.bookfloww.dto.google;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleEventCreateRequestDto {
	
	private String summary;
	private String description;
	private GoogleEventDateTimeDto start;
	private GoogleEventDateTimeDto end;
	private List<Attendee> attendees;
	private ConferenceData conferenceData;
	
	@Getter
	@Setter
	public static class Attendee{
		private String email;
		private Boolean self;
	}
	
	@Getter
	@Setter
	public static class ConferenceData{
		private CreateRequest createRequest;
	}
	
	@Getter
	@Setter
	public static class CreateRequest{
		private String requestId;
		private ConferenceSolutionKey conferenceSolutionKey;
	}
	
	@Getter
	@Setter
	public static class ConferenceSolutionKey{
		private String type;
	}

}
