package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.domain.service.LocationService;
import dev.sorokin.eventmanager.controller.dto.LocationDto;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll() {
        log.debug("getting all locations");
        var locations = locationService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locations.stream()
                        .map(LocationMapper::toDto)
                        .toList()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {
        log.debug("getting location by id: {}", id);
        var location = locationService.getLocation(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LocationMapper.toDto(location));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto request) {
        log.debug("creating location by dto: {}", request);
        var location = locationService.createLocation(LocationMapper.toDomain(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(LocationMapper.toDto(location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("deleting location by id: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable Long id,
            @RequestBody @Valid LocationDto request) {
        log.debug("updating location by id: {}, dto: {}", id, request);
        var location = locationService.updateLocation(id, LocationMapper.toDomain(request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LocationMapper.toDto(location));
    }
}
