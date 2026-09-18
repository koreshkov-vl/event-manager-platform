package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.controller.dto.LocationDto;
import dev.sorokin.eventmanager.persistence.entity.LocationEntity;

public final class LocationMapper {

    private LocationMapper() {}

    public static LocationDto toDto(Location entity) {
        return new LocationDto(
                entity.id(),
                entity.name(),
                entity.address(),
                entity.capacity(),
                entity.description()
        );
    }

    public static Location toDomain(LocationDto dto) {
        return new Location(
                null,
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.description()
        );
    }

    public static Location toDomain(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCapacity(),
                entity.getDescription()
        );
    }

    public static LocationEntity toEntity(Location domain) {
        return new LocationEntity(
                domain.id(),
                domain.name(),
                domain.address(),
                domain.capacity(),
                domain.description(),
                null
        );
    }
}
