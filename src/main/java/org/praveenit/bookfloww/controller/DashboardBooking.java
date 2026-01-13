package org.praveenit.bookfloww.controller;

import java.time.Instant;
import java.util.List;

import org.praveenit.bookfloww.dto.BookingCreateRequest;
import org.praveenit.bookfloww.dto.BookingResponseDto;
import org.praveenit.bookfloww.dto.BookingUpdateRequest;
import org.praveenit.bookfloww.entity.BookingEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.service.google.BookingService;
import org.praveenit.bookfloww.service.google.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard/bookings")
@RequiredArgsConstructor
public class DashboardBooking {
	private final BookingService bookingService;
	private final SecurityUtil securityUtil;
	
	//get all bookings
	@GetMapping("/getAllBookings")
	public ResponseEntity<List<BookingResponseDto>> getAllBookings(){
		User user=securityUtil.getCurrentUser();
		return ResponseEntity.ok(bookingService.getMyBookings(user));
	}
	
	//get single booking
	@GetMapping("/getBooking/{id}")
	public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long id){
		User user=securityUtil.getCurrentUser();
		return ResponseEntity.ok(bookingService.getBooking(user, id));
	}
	
	//create bookfloww booking
	@PostMapping("/createBooking")
	public ResponseEntity<BookingResponseDto> createBooking(
			@RequestBody BookingCreateRequest request){
		
		User user = securityUtil.getCurrentUser();
		
		BookingResponseDto response =
	            bookingService.createBookflowwBooking(
	                    user,
	                    request.getStart(),
	                    request.getEnd(),
	                    request.getCustomerName(),
	                    request.getMeetingDescription(),
	                    request.getCustomerEmail()
	            );

	    return ResponseEntity.ok(response);
	}
	
	//update
	@PatchMapping("/updateBooking/{id}")
	public ResponseEntity<BookingResponseDto> updateBooking(
			@PathVariable Long id,
			@RequestBody BookingUpdateRequest bookingUpdateRequest){
		User user=securityUtil.getCurrentUser();
		return ResponseEntity.ok(bookingService.updateBookingTime(
				user, 
				id, 
				bookingUpdateRequest.getStart(), 
				bookingUpdateRequest.getEnd()));
	}
	
	//cancel
	@DeleteMapping("/cancelBooking/{id}")
	public ResponseEntity<?> cancelBooking(@PathVariable Long id){
		User user=securityUtil.getCurrentUser();
		bookingService.cancelBooking(user, id);
		return ResponseEntity.ok("Booking cancelled");
	}

}
