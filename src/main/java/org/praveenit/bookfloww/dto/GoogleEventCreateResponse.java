package org.praveenit.bookfloww.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleEventCreateResponse {
	
	private String id;
	private String hangoutLink;
	private ConferenceResponseData conferenceData;
	
	@Getter
	@Setter
	public static class ConferenceResponseData {
		private List<EntryPoint> entryPoints;
	}
	
	@Getter
	@Setter
	public static class EntryPoint {
		private String entryPointType;
		private String uri;
	}

}
