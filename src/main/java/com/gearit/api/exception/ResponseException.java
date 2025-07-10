package com.gearit.api.exception;

public class ResponseException {

    private String message;

    public ResponseException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
