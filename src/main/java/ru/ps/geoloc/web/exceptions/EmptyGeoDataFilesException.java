package ru.ps.geoloc.web.exceptions;


public class EmptyGeoDataFilesException extends RestServiceException {
    public EmptyGeoDataFilesException() {
        super();
    }

    public EmptyGeoDataFilesException(String message) {
        super(message);
    }

    public EmptyGeoDataFilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
