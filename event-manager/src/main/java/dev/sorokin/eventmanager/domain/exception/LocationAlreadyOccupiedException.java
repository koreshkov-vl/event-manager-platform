package dev.sorokin.eventmanager.domain.exception;

public class LocationAlreadyOccupiedException extends RuntimeException {
    public LocationAlreadyOccupiedException(String message) {
        super(message);
    }
}
