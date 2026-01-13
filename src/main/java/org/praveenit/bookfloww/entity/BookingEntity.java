package org.praveenit.bookfloww.entity;

import java.time.Instant;

import org.praveenit.bookfloww.enums.EventSource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
	    name = "bookings",
	    uniqueConstraints = {
	    		// Prevent duplicate Google sync
	        @UniqueConstraint(columnNames = {"google_event_id", "user_id"}),
	           // Prevent double booking for same user & slot
	        @UniqueConstraint(columnNames = {"user_id", "start_time", "end_time"})
	    }
	)
public class BookingEntity extends AuditStamp {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Who booked */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Time slot */
    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    /** External reference */
    @Column(name = "google_event_id", length = 128)
    private String googleEventId;

     /** Where booking came from */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventSource source;

    /** Booking state */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    
    private String customerName;
    
    private String customerEmail;
    
    @Column(length = 500)
    private String meetingDescription;

    private String meetLink;

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED
    }
    
    // DOMAIN HELPERS
    public boolean isGoogleSource() {
        return EventSource.GOOGLE.equals(this.source);
    }

    public boolean isBookflowwSource() {
        return EventSource.BOOKFLOWW.equals(this.source);
    }

}
