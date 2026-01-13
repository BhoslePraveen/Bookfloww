package org.praveenit.bookfloww.service.google;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.praveenit.bookfloww.dto.google.GoogleEventDateTimeDto;
import org.praveenit.bookfloww.dto.google.GoogleEventItemDto;
import org.praveenit.bookfloww.entity.GoogleEventEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.enums.EventSource;
import org.praveenit.bookfloww.repository.GoogleEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleEventService {
	private final GoogleEventRepository googleEventRepository;
	
	public void syncGoogleEvents(User user, List<GoogleEventItemDto> items) {

	    for (GoogleEventItemDto item : items) {

	        Optional<GoogleEventEntity> existing =
	                googleEventRepository
	                        .findByGoogleEventIdAndUser_Id(item.getId(), user.getId());
	        
	        //  Handle cancelled events FIRST
	        if ("cancelled".equalsIgnoreCase(item.getStatus())) {

	            existing.ifPresent(e -> {
	                e.setStatus("cancelled");
	                googleEventRepository.save(e);
	            });

	            // do not create new event if it is cancelled
	            continue;
	        }

	     //  Normal create / update flow
	        GoogleEventEntity event = existing.orElseGet(() -> {
	            GoogleEventEntity e = new GoogleEventEntity();
	            e.setGoogleEventId(item.getId());
	            e.setUser(user);
	            e.setSource(EventSource.GOOGLE);	            
	            return e;
	        });
	        
	       // Safety in case of old rows
	        if (event.getSource() == null) {
	            event.setSource(EventSource.GOOGLE);
	        }
	        
	        event.setSummary(
	                item.getSummary() != null ? item.getSummary() : "No Title");
	        event.setStatus(item.getStatus());
	        event.setStartTime(parseGoogleDate(item.getStart()));
	        event.setEndTime(parseGoogleDate(item.getEnd()));

	        googleEventRepository.save(event);
	    }
	}
	 /**
     * Handles both dateTime and date (all-day events)
     */
    private Instant parseGoogleDate(GoogleEventDateTimeDto dto) {
        if (dto.getDateTime() != null) {
        	return Instant.parse(dto.getDateTime());
        }
        // all-day event → start of day UTC
        return Instant.parse(dto.getDate() + "T00:00:00Z");
    }

}
