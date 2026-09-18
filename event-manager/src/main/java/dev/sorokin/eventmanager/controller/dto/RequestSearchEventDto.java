package dev.sorokin.eventmanager.controller.dto;

import java.time.OffsetDateTime;

public record RequestSearchEventDto(
        Integer durationMin,
        Integer durationMax,
        OffsetDateTime dateStartBefore,
        OffsetDateTime dateStartAfter,
        Integer placesMin,
        Integer placesMax,
        Integer costMin,
        Integer costMax,
        Long locationId,
        String eventStatus,
        String name
) {}