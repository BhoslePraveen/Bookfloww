package org.praveenit.bookfloww.service.google;

import java.time.Instant;
import java.util.List;
import org.praveenit.bookfloww.dto.BookingResponseDto;
import org.praveenit.bookfloww.dto.GoogleEventCreateResponse;
import org.praveenit.bookfloww.entity.BookingEntity;
import org.praveenit.bookfloww.entity.GoogleEventEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.enums.EventSource;
import org.praveenit.bookfloww.repository.BookingRepository;
import org.praveenit.bookfloww.repository.GoogleEventRepository;
import org.praveenit.bookfloww.security.impl.BookingError;
import org.praveenit.bookfloww.security.impl.BookingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;
	private final GoogleEventRepository googleEventRepository;
	private final GoogleCalenderService googleCalenderService;
	private final CalenderSyncService calenderSyncService;
	
	//create by BOOKFLOWW
	@Transactional
	public BookingResponseDto createBookflowwBooking(
			User user, 
			Instant start, 
			Instant end, 
			String customerName,
	        String description,
	        String email) {
		
		// Prevent overlap ONLY for Bookfloww
		if (bookingRepository.existsOverlappingBooking(
	            user.getId(), 
	            start, 
	            end, 
	            BookingEntity.BookingStatus.CANCELLED)) {
			throw new BookingException(BookingError.SLOT_ALREADY_BOOKED);
	    }
		
		//Create booking (WITHOUT google fields)
		BookingEntity booking=new BookingEntity();
		booking.setUser(user);
		booking.setStartTime(start);
		booking.setEndTime(end);
		booking.setSource(EventSource.BOOKFLOWW);
		booking.setStatus(BookingEntity.BookingStatus.CONFIRMED);
		booking.setCustomerName(customerName);
		booking.setCustomerEmail(email);
		booking.setMeetingDescription(description);
		
		booking= bookingRepository.save(booking);
		
		//create google event
		//get Access token
		String accessToken=calenderSyncService.getGoogleAccessToken(user);
		
		//Create Google event + Meet
		GoogleEventCreateResponse response=googleCalenderService.createEvent(accessToken, booking);
		
		//Set Google Event ID
		booking.setGoogleEventId(response.getId());
		booking.setMeetLink(extractMeetLink(response));
		
		// SAVE GOOGLE EVENT ENTITY 
	    GoogleEventEntity googleEvent = new GoogleEventEntity();
	    googleEvent.setGoogleEventId(response.getId());
	    googleEvent.setUser(user);
	    googleEvent.setSummary("Bookfloww booking");
	    googleEvent.setStatus("confirmed");
	    googleEvent.setStartTime(start);
	    googleEvent.setEndTime(end);
	    googleEvent.setSource(EventSource.BOOKFLOWW);
		
	    googleEventRepository.save(googleEvent);
	    
		booking=bookingRepository.save(booking);
		return mapToDto(booking);
	}
	//Get
	public List<BookingResponseDto> getMyBookings(User user){
		return bookingRepository.findByUser_Id(user.getId())
				.stream()
				.map(this::mapToDto)
				.toList();
	}
	
	public BookingResponseDto getBooking(User user, Long bookingId) {
		BookingEntity booking = getBookingEntity(user, bookingId);
        return mapToDto(booking);
	}
	
	private BookingEntity getBookingEntity(User user, Long bookingId) {
	    return bookingRepository.findByIdAndUser_Id(bookingId, user.getId())
	            .orElseThrow(() -> new BookingException(BookingError.BOOKING_NOT_FOUND));
	}
	//update
	@Transactional
	public BookingResponseDto updateBookingTime(User user, Long bookingId, Instant start, Instant end) {
		BookingEntity booking=getBookingEntity(user, bookingId);
		
		// Google bookings are read-only
		if (booking.isGoogleSource()) {
			throw new BookingException(BookingError.GOOGLE_BOOKING_READ_ONLY);
        }
		
		 // Prevent overlapping slot (excluding current booking)
	    boolean overlap = bookingRepository
	            .existsOverlappingBookingForUpdate(
	                    user.getId(), 
	                    bookingId, 
	                    start, 
	                    end,
	                    BookingEntity.BookingStatus.CANCELLED);

	    if (overlap) {
	    	throw new BookingException(BookingError.SLOT_ALREADY_BOOKED);
	    }
	    
		booking.setStartTime(start);
		booking.setEndTime(end);
		
		String accessToken=calenderSyncService.getGoogleAccessToken(user);
		googleCalenderService.updateBooking(accessToken, booking);
		
		//UPDATE GOOGLE EVENT ENTITY
		googleEventRepository
		    .findByGoogleEventIdAndUser_Id(booking.getGoogleEventId(), user.getId())
		    .ifPresent(event -> {
		        event.setStartTime(start);
		        event.setEndTime(end);
		    });
		
		booking=bookingRepository.save(booking);
		return mapToDto(booking);
	}
	//Delete
	public void cancelBooking(User user, Long bookingId) {
		BookingEntity booking=getBookingEntity(user, bookingId);
		// Google bookings are READ-ONLY
	    if (booking.isGoogleSource()) {
	        throw new BookingException(BookingError.GOOGLE_BOOKING_READ_ONLY);
	    }
		booking.setStatus(BookingEntity.BookingStatus.CANCELLED);
		
		//Cancel Google event ONLY if Bookfloww booking
		if (booking.isBookflowwSource() && booking.getGoogleEventId() != null) {
			
			try {
            String accessToken =
                    calenderSyncService.getGoogleAccessToken(user);

            googleCalenderService.cancelEvent(
                    accessToken, booking.getGoogleEventId());
			}catch (Exception e) {
	            // log only, do not rollback DB cancel
	            log.error("Failed to cancel Google event for booking {}", bookingId, e);
	        }
        }
		
	    //keep GoogleEventEntity in sync
		googleEventRepository
	    .findByGoogleEventIdAndUser_Id(booking.getGoogleEventId(), user.getId())
	    .ifPresent(event -> event.setStatus("cancelled"));
		
		bookingRepository.save(booking);
	}
	
	private BookingResponseDto mapToDto(BookingEntity booking) {
	    BookingResponseDto dto = new BookingResponseDto();
	    dto.setId(booking.getId());
	    dto.setStartTime(booking.getStartTime());
	    dto.setEndTime(booking.getEndTime());
	    dto.setStatus(booking.getStatus().name());
	    dto.setSource(booking.getSource().name());
	    dto.setGoogleEventId(booking.getGoogleEventId());
	    dto.setCustomerName(booking.getCustomerName());
	    dto.setCustomerEmail(booking.getCustomerEmail());
	    dto.setMeetingDescription(booking.getMeetingDescription());
	    dto.setMeetLink(booking.getMeetLink());
	    return dto;
	}
	
	private String extractMeetLink(GoogleEventCreateResponse response) {
        if (response.getHangoutLink() != null) {
            return response.getHangoutLink();
        }
        if (response.getConferenceData() != null) {
            return response.getConferenceData()
                    .getEntryPoints()
                    .stream()
                    .filter(e -> "video".equals(e.getEntryPointType()))
                    .map(e -> e.getUri())
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
