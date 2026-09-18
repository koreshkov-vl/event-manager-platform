package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.exception.EventNotFoundException;
import dev.sorokin.eventmanager.domain.exception.RegistrationException;
import dev.sorokin.eventmanager.domain.exception.RegistrationNotFoundException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.persistence.entity.RegistrationEntity;
import dev.sorokin.eventmanager.persistence.repository.EventRepository;
import dev.sorokin.eventmanager.persistence.repository.RegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static dev.sorokin.eventmanager.persistence.entity.EventStatus.FINISHED;
import static dev.sorokin.eventmanager.persistence.entity.EventStatus.STARTED;
import static dev.sorokin.eventmanager.persistence.entity.EventStatus.WAIT_START;

@Component
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final GetterUserService getterUserService;

    public RegistrationService(
            EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            GetterUserService getterUserService
    ) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.getterUserService = getterUserService;
    }

    @Transactional
    public void createRegistration(Long eventId) {
        var event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found by id: " + eventId));

        var user = getterUserService.getUserFromContext();
        var existedRegistration = registrationRepository.findByEventIdAndUserId(eventId, user.getId());
        if (existedRegistration.isPresent()) {
            throw new RegistrationException("You have already registered");
        }

        if (!event.getStatus().equals(WAIT_START)) {
            throw new RegistrationException("Event in wrong status: " + event.getStatus() + ". expected: WAIT_START");
        }

        if (event.getOccupiedPlaces().equals(event.getMaxPlaces())) {
            throw new RegistrationException("Event is full");
        }

        RegistrationEntity registration = new RegistrationEntity();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setCreatedAt(OffsetDateTime.now());
        registrationRepository.save(registration);

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        eventRepository.save(event);
    }

    @Transactional
    public void cancelRegistration(Long eventId) {
        var event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found by id: " + eventId));
        if (event.getStatus().equals(STARTED) || event.getStatus().equals(FINISHED)) {
            throw new RegistrationException("Event has already started or finished");
        }

        if (event.getOccupiedPlaces() != 0) {
            event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        }

        var registration = registrationRepository.findByEventIdAndUserId(eventId, getterUserService.getUserFromContext().getId());
        if (registration.isEmpty()) {
            throw new RegistrationNotFoundException("Registration not found for event id: " + eventId);
        }

        registrationRepository.deleteById(registration.get().getId());
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<Event> getMyRegistrations() {
        var events = eventRepository.findEventRegistrationsByUserId(getterUserService.getUserFromContext().getId());
        return events.stream()
                .map(EventMapper::toDomain)
                .toList();
    }
}
