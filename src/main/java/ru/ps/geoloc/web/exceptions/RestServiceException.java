package ru.ps.geoloc.web.exceptions;

public class RestServiceException extends Exception {
    public RestServiceException() {
    }

    public RestServiceException(String message) {
        super(message);
    }

    public RestServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
