package dev.sorokin.eventnotificator.persistence.entity;

import dev.sorokin.eventnotificator.controller.dto.ChangeItem;

import java.util.List;

public record NotificationEventPayloadJson(
        String eventName,
        Long changedById,
        List<ChangeItem> changes
) {}
