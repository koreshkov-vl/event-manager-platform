package dev.sorokin.eventmanager.controller.dto;

import java.time.OffsetDateTime;

public record ResponseEventDto(
        Integer occupiedPlaces,
        OffsetDateTime date,
        Integer duration,
        Integer cost,
        Integer maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        String status
) {}