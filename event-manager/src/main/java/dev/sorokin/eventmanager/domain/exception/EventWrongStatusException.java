package dev.sorokin.eventmanager.domain.exception;

public class EventWrongStatusException extends RuntimeException {
    public EventWrongStatusException(String message) {
        super(message);
    }
}
