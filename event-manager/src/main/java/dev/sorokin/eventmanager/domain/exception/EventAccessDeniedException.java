package dev.sorokin.eventmanager.domain.exception;

public class EventAccessDeniedException extends RuntimeException {
    public EventAccessDeniedException(String message) {
        super(message);
    }
}
