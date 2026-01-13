package org.praveenit.bookfloww.dto.google;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GoogleEventUpdateRequest {

    private EventDateTime start;
    private EventDateTime end;

    @Getter @Setter
    public static class EventDateTime {
        private String dateTime;
    }
}
