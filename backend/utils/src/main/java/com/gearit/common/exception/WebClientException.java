package com.gearit.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


@JsonIgnoreProperties({"stackTrace", "localizedMessage", "suppressed", "cause"})
public class WebClientException extends RuntimeException {

    private final String message;

    @Getter
    private final String extendedHelp;

    public WebClientException(String message, String extendedHelp) {
        this.message = message;
        this.extendedHelp = extendedHelp;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
