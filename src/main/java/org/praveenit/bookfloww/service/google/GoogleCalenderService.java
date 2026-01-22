package org.praveenit.bookfloww.service.google;

import java.util.List;
import org.praveenit.bookfloww.dto.GoogleEventCreateResponse;
import org.praveenit.bookfloww.dto.google.GoogleEventCreateRequestDto;
import org.praveenit.bookfloww.dto.google.GoogleEventDateTimeDto;
import org.praveenit.bookfloww.dto.google.GoogleEventUpdateRequest;
import org.praveenit.bookfloww.dto.google.GoogleEventsResponseDto;
import org.praveenit.bookfloww.entity.BookingEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalenderService {

	private final RestTemplate restTemplate;
	
	@Value("${google.calendar.events.url}")
	private String googleEventsUrl;

	public GoogleEventsResponseDto fetchEvents(String googleAccessToken) {
		log.info("Fetching google calender events");
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(googleAccessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<GoogleEventsResponseDto> response = restTemplate.exchange(googleEventsUrl, HttpMethod.GET, entity,
				GoogleEventsResponseDto .class);
		return response.getBody();
	}

	// create (Bookfloww->google)
	public GoogleEventCreateResponse createEvent(String accessToken, BookingEntity booking) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		
		GoogleEventDateTimeDto start=new GoogleEventDateTimeDto(booking.getStartTime().toString(), null);
		
		GoogleEventDateTimeDto end=new GoogleEventDateTimeDto(booking.getEndTime().toString(), null);
		
		GoogleEventCreateRequestDto.Attendee organizer=new GoogleEventCreateRequestDto.Attendee();
		organizer.setEmail(booking.getUser().getEmail());
		organizer.setSelf(true);
		
		GoogleEventCreateRequestDto.Attendee customer=new GoogleEventCreateRequestDto.Attendee();
		customer.setEmail(booking.getCustomerEmail());
		customer.setSelf(false);
		
		String requestId = "bookfloww-" + booking.getId() + "-" + System.currentTimeMillis();
		
		GoogleEventCreateRequestDto.ConferenceSolutionKey key=new GoogleEventCreateRequestDto.ConferenceSolutionKey();
		key.setType("hangoutsMeet");
		
		GoogleEventCreateRequestDto.CreateRequest createRequest=new GoogleEventCreateRequestDto.CreateRequest();
		createRequest.setRequestId(requestId);
		createRequest.setConferenceSolutionKey(key);
		
		GoogleEventCreateRequestDto.ConferenceData conferenceData=new GoogleEventCreateRequestDto.ConferenceData();
		conferenceData.setCreateRequest(createRequest);
		
		GoogleEventCreateRequestDto request=new GoogleEventCreateRequestDto();
		request.setSummary("Bookfloww booking");
		request.setDescription(booking.getMeetingDescription());
		request.setStart(start);
		request.setEnd(end);
		request.setAttendees(List.of(customer));
		request.setConferenceData(conferenceData);

		HttpEntity<GoogleEventCreateRequestDto> entity = new HttpEntity<>(request, headers);

		ResponseEntity<GoogleEventCreateResponse> response = restTemplate.exchange(
				googleEventsUrl + "?conferenceDataVersion=1&sendUpdates=all", 
				HttpMethod.POST,
				entity, 
				GoogleEventCreateResponse.class);
		
		try {
		    return response.getBody();
		} catch (HttpClientErrorException e) {
		    throw e;
		}

	}

	// update googleEvent
	public void updateBooking(String accessToken, BookingEntity booking) {
		String url = googleEventsUrl + "/" + booking.getGoogleEventId() + "?sendUpdates=all";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		GoogleEventUpdateRequest request=new GoogleEventUpdateRequest();
		GoogleEventUpdateRequest.EventDateTime start=new GoogleEventUpdateRequest.EventDateTime();
		start.setDateTime(booking.getStartTime().toString());
		
		GoogleEventUpdateRequest.EventDateTime end=new GoogleEventUpdateRequest.EventDateTime();
		end.setDateTime(booking.getEndTime().toString());
		
		request.setStart(start);
		request.setEnd(end);

		HttpEntity<GoogleEventUpdateRequest> entity = new HttpEntity<>(request, headers);

		restTemplate.exchange(url, HttpMethod.PATCH, entity, Void.class);
	}

	// cancel event
	public void cancelEvent(String accessToken, String googleEventId) {
		String url = googleEventsUrl + "/" + googleEventId;

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}
}
