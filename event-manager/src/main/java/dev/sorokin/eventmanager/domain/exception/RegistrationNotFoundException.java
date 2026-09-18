package dev.sorokin.eventmanager.domain.exception;

public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(String message) {
        super(message);
    }
}
