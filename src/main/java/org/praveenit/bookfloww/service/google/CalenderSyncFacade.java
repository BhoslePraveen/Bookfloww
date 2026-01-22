package org.praveenit.bookfloww.service.google;

import org.praveenit.bookfloww.dto.google.GoogleEventsResponseDto;
import org.praveenit.bookfloww.entity.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalenderSyncFacade {
	private final CalenderFacadeService calenderFacadeService;
	private final GoogleEventService googleEventService;
	
	public void syncCalenderForUser(User user) {
		GoogleEventsResponseDto response=calenderFacadeService.fetchThreeMonthEvents();
			
		googleEventService.syncGoogleEvents(user, response.getItems());
		
	}
	

}
