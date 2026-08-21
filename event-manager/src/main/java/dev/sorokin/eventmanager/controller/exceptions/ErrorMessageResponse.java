package dev.sorokin.eventmanager.controller.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorMessageResponse(String title, List<ErrorMessage> messages, LocalDateTime time) {}

record ErrorMessage(String title, String message) {}
