package org.praveenit.bookfloww.dto.google;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleEventsResponseDto {
	
	    private String kind;
	    private List<GoogleEventItemDto> items;
}
