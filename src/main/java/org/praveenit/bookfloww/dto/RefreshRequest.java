package org.praveenit.bookfloww.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}

