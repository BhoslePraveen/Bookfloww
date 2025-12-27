package org.praveenit.bookfloww.security.impl;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String error;
    private String errorDescription;
    private String action;
}
