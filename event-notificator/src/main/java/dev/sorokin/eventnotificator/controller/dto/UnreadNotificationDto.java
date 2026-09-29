package dev.sorokin.eventnotificator.controller.dto;

import java.time.LocalDateTime;

public record UnreadNotificationDto(
        Long notificationId,
        String type,
        Long eventId,
        LocalDateTime createdAt,
        Boolean isRead,
        String message,
        NotificationPayload payload
) {}
