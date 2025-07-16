package com.gearit.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WebClientException extends RuntimeException {

    private final String message;

    private final String extendedHelp;

    public WebClientException(String message, String extendedHelp) {
        this.message = message;
        this.extendedHelp = extendedHelp;
    }
}
