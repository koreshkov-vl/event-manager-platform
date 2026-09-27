package dev.sorokin.eventnotificator.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record NotificationPayload(
        String eventType,
        LocalDateTime occurredAt,
        Long changedById,
        Long ownerId,
        String eventName,
        List<ChangeItem> changes
) {}