package dev.sorokin.eventnotificator.controller.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RequestNotificationIdsDto(
        @NotEmpty
        List<Long> notificationIds
) {}
