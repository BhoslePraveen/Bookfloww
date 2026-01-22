package org.praveenit.bookfloww.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class BookingUpdateRequest {
    private Instant start;
    private Instant end;
}
