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
public class GoogleEventItemDto {
	private String id;
    private String status;
    private String summary;
    private GoogleEventDateTimeDto start;
    private GoogleEventDateTimeDto end;

}
