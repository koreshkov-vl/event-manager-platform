package dev.sorokin.eventmanager.domain;

import java.time.OffsetDateTime;

public record Event(
        Long id,
        String name,
        OffsetDateTime startAt,
        Integer durationMinutes,
        Integer maxPlaces,
        Integer occupiedPlaces,
        Integer cost,
        String status,
        Long locationId,
        Long userId
) {}