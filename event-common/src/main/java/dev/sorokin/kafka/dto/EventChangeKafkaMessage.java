package dev.sorokin.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventChangeKafkaMessage(
        UUID messageId,
        String eventType,
        Long eventId,
        LocalDateTime occurredAt,
        Long ownerId,
        Long changedById,
        List<Long> subscribers,
        List<ChangeItem> changes
) {}
