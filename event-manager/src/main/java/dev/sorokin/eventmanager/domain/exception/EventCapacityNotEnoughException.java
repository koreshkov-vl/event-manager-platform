package dev.sorokin.eventmanager.domain.exception;

public class EventCapacityNotEnoughException extends RuntimeException {
    public EventCapacityNotEnoughException(String message) {
        super(message);
    }
}
