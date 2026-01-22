package org.praveenit.bookfloww.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;

import org.praveenit.bookfloww.enums.EventSource;

@Setter
@Getter
@Entity
@Table(
    name = "google_events",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"google_event_id", "user_id"})
    }
)
public class GoogleEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Google unique event ID
    @Column(name = "google_event_id", nullable = false)
    private String googleEventId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventSource source;

    // Event title (used in booking UI)
    @Column(nullable = false)
    private String summary;

    // confirmed / cancelled
    @Column(nullable = false)
    private String status;

    // Booking start time
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    // Booking end time
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    // Owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
