package com.gearit.common.http.response;

public record WebClientExceptionResponse(
        String message,
        String extendedHelp
) {
}
