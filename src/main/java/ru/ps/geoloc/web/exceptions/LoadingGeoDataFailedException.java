package ru.ps.geoloc.web.exceptions;

public class LoadingGeoDataFailedException extends Exception {

    public LoadingGeoDataFailedException() {
    }

    public LoadingGeoDataFailedException(String message) {
        super(message);
    }
}
