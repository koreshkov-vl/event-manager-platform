package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.domain.exception.LocationNotFoundException;
import dev.sorokin.eventmanager.persistence.repository.LocationRepository;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<Location> getAll() {
        return locationRepository.findAll()
                .stream()
                .map(LocationMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public Location getLocation(Long id) {
        return locationRepository.findById(id)
                .map(LocationMapper::toDomain)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + id));
    }

    public Location createLocation(Location location) {
        var entity = locationRepository.save(LocationMapper.toEntity(location));
        return LocationMapper.toDomain(entity);
    }

    public void deleteLocation(Long id) {
        locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + id));
        locationRepository.deleteById(id);
    }

    @Transactional
    public Location updateLocation(Long id, Location location) {
        var locationEntity = locationRepository.findById(id)
                .map( entity -> {
                        entity.setName(location.name());
                        entity.setAddress(location.address());
                        entity.setCapacity(location.capacity());
                        entity.setDescription(location.description());
                        return locationRepository.save(entity);
                })
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + id));
        return LocationMapper.toDomain(locationEntity);
    }
}
