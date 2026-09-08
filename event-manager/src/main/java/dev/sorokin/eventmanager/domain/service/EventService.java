package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.controller.dto.RequestEventDto;
import dev.sorokin.eventmanager.controller.dto.RequestSearchEventDto;
import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.exception.EventAccessDeniedException;
import dev.sorokin.eventmanager.domain.exception.EventCapacityNotEnoughException;
import dev.sorokin.eventmanager.domain.exception.EventNotFoundException;
import dev.sorokin.eventmanager.domain.exception.EventWrongStatusException;
import dev.sorokin.eventmanager.domain.exception.LocationNotFoundException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.persistence.EventSpecifications;
import dev.sorokin.eventmanager.persistence.entity.EventEntity;
import dev.sorokin.eventmanager.persistence.entity.EventStatus;
import dev.sorokin.eventmanager.persistence.entity.LocationEntity;
import dev.sorokin.eventmanager.persistence.entity.UserRole;
import dev.sorokin.eventmanager.persistence.repository.EventRepository;
import dev.sorokin.eventmanager.persistence.repository.LocationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static dev.sorokin.eventmanager.persistence.entity.EventStatus.CANCELLED;
import static dev.sorokin.eventmanager.persistence.entity.EventStatus.WAIT_START;

@Component
public class EventService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final GetterUserService getterUserService;

    public EventService(
            LocationRepository locationRepository,
            EventRepository eventRepository,
            GetterUserService getterUserService) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.getterUserService = getterUserService;
    }

    @Transactional
    public Event createEvent(@Valid RequestEventDto request) {
        var location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new LocationNotFoundException("Location not found by id: " + request.locationId()));

        if (location.getCapacity() < request.maxPlaces()) {
            throw new EventCapacityNotEnoughException("Location capacity is not enough. Available: " + location.getCapacity() +
                    ", required: " + request.maxPlaces());
        }

        EventEntity eventEntity = new EventEntity(
                request.name(),
                request.date(),
                request.duration(),
                request.maxPlaces(),
                request.cost(),
                WAIT_START,
                location,
                getterUserService.getUserFromContext()
        );
        var savedEvent = eventRepository.save(eventEntity);
        return EventMapper.toDomain(savedEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found by id: " + eventId));

        if (!isOwnerOrAdmin(event.getUser().getId())) {
            throw new EventAccessDeniedException("You must be owner for this event");
        }

        if (event.getStartAt().isBefore(OffsetDateTime.now())) {
            throw new EventWrongStatusException("The event is already in progress and cannot be deleted");
        }

        event.setStatus(CANCELLED);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .map(EventMapper::toDomain)
                .orElseThrow(() -> new EventNotFoundException("Event not found by id: " + eventId));
    }

    @Transactional
    public Event updateEvent(@NotNull Long eventId, @Valid RequestEventDto request) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found by id: " + eventId));

        if (!isOwnerOrAdmin(event.getUserId())) {
            throw new EventAccessDeniedException("You must be owner for this event");
        }

        LocationEntity location = null;
        if (event.getLocationId().equals(request.locationId())) {
            location = event.getLocation();
        } else {
            location = locationRepository.findById(request.locationId())
                    .orElseThrow(() -> new LocationNotFoundException("Location not found by id: " + request.locationId()));
        }

        if (request.maxPlaces() < event.getOccupiedPlaces() ||
                request.maxPlaces() > location.getCapacity()) {
            throw new EventCapacityNotEnoughException("Max places can not be less than occupied places or more than location capacity");
        }

        event.setStartAt(request.date());
        event.setDurationMinutes(request.duration());
        event.setCost(request.cost());
        event.setMaxPlaces(request.maxPlaces());
        event.setLocation(location);
        event.setName(request.name());

        var savedEvent = eventRepository.save(event);
        return EventMapper.toDomain(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<Event> searchEvent(RequestSearchEventDto request) {
        EventStatus status = request.eventStatus() != null ? EventStatus.valueOf(request.eventStatus()) : null;
        var events = eventRepository.findAll(
                EventSpecifications.nameEquals(request.name())
                        .and(EventSpecifications.placesMin(request.placesMin()))
                        .and(EventSpecifications.placesMax(request.placesMax()))
                        .and(EventSpecifications.dateStartBefore(request.dateStartBefore()))
                        .and(EventSpecifications.dateStartAfter(request.dateStartAfter()))
                        .and(EventSpecifications.costMin(request.costMin()))
                        .and(EventSpecifications.costMax(request.costMax()))
                        .and(EventSpecifications.durationMin(request.durationMin()))
                        .and(EventSpecifications.durationMax(request.durationMax()))
                        .and(EventSpecifications.locationIdEquals(request.locationId()))
                        .and(EventSpecifications.statusEquals(status))
        );
        return events.stream()
                .map(EventMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Event> getMyEvent() {
        var currentUserId = getterUserService.getUserFromContext().getId();
        var events = eventRepository.findByUserId(currentUserId);
        return events.stream()
                .map(EventMapper::toDomain)
                .toList();
    }

    private boolean isOwnerOrAdmin(Long ownerId) {
        var user = getterUserService.getUserFromContext();
        return user.getId().equals(ownerId) || user.getRole().equals(UserRole.ADMIN);
    }
}
