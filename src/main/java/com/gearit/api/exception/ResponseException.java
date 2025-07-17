package com.gearit.api.exception;

public class ResponseException {

    private final String message;
    private String extendedHelp;

    public ResponseException(String message) {
        this.message = message;
    }

    public ResponseException(String message, String extendedHelp) {
        this.message = message;
        this.extendedHelp = extendedHelp;
    }

    public String getMessage() {
        return message;
    }

    public String getExtendedHelp() {
        return extendedHelp;
    }
}
