package org.praveenit.bookfloww.dto.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleEventDateTimeDto {
	
	private String dateTime; // for timed events
    private String date;
 
}
