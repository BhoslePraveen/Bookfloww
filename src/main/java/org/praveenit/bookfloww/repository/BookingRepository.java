package org.praveenit.bookfloww.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.praveenit.bookfloww.entity.BookingEntity;
import org.praveenit.bookfloww.entity.BookingEntity.BookingStatus;
import org.praveenit.bookfloww.enums.EventSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

	public boolean existsByGoogleEventIdAndUser_Id(String googleEventId, String userId);
	
	public Optional<BookingEntity> findByIdAndUser_Id(Long id, String userId);
	
	public Optional<BookingEntity> findByGoogleEventIdAndUser_Id(String googleEventId, String userId);
	
	public List<BookingEntity> findByUser_Id(String userId);
	
	//OVERLAP CHECK (CREATE)
	@Query("""
		    SELECT COUNT(b) > 0
		    FROM BookingEntity b
		    WHERE b.user.id = :userId
		      AND b.status <> :cancelled
		      AND b.startTime < :end
		      AND b.endTime > :start
		""")
		boolean existsOverlappingBooking(
		        @Param("userId") String userId,
		        @Param("start") Instant start,
		        @Param("end") Instant end,
		        @Param("cancelled") BookingStatus cancelled
		);
	
	@Query("""
			SELECT COUNT(b) > 0 
			FROM BookingEntity b
			WHERE b.user.id = :userId
			AND b.id <> :bookingId
			AND b.status = :confirmed
			AND b.startTime < :end
			AND b.endTime > :start
			""")
			boolean existsOverlappingBookingForUpdate(
			        String userId,
			        Long bookingId,
			        Instant start,
			        Instant end,
			        @Param("confirmed") BookingStatus confirmed);
	
	//REPORTING / ANALYTICS
	public long countByUser_Id(String userId);

    public long countByUser_IdAndSource(String userId, EventSource source);


}
