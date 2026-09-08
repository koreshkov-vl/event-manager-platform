package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.controller.dto.ResponseEventDto;
import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.persistence.entity.EventEntity;

public final class EventMapper {

    private EventMapper() {}

    public static Event toDomain(EventEntity entity) {
        return new Event(
            entity.getId(),
            entity.getName(),
            entity.getStartAt(),
            entity.getDurationMinutes(),
            entity.getMaxPlaces(),
            entity.getOccupiedPlaces(),
            entity.getCost(),
            entity.getStatus().toString(),
            entity.getLocationId(),
            entity.getUserId()
        );
    }

    public static ResponseEventDto toDto(Event domain) {
        return new ResponseEventDto(
                domain.occupiedPlaces(),
                domain.startAt(),
                domain.durationMinutes(),
                domain.cost(),
                domain.maxPlaces(),
                domain.locationId(),
                domain.name(),
                domain.id(),
                domain.userId(),
                domain.status()
        );
    }
}
