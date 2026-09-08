package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.controller.dto.RequestEventDto;
import dev.sorokin.eventmanager.controller.dto.RequestSearchEventDto;
import dev.sorokin.eventmanager.controller.dto.ResponseEventDto;
import dev.sorokin.eventmanager.domain.service.EventService;
import dev.sorokin.eventmanager.mapper.EventMapper;
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
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<ResponseEventDto> createEvent(@RequestBody @Valid RequestEventDto request) {
        log.debug("creating event by dto: {}", request);
        var event = eventService.createEvent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EventMapper.toDto(event));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        log.debug("deleting event by id: {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ResponseEventDto> getEvent(@PathVariable Long eventId) {
        log.debug("getting event by id: {}", eventId);
        var event = eventService.getEvent(eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EventMapper.toDto(event));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ResponseEventDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid RequestEventDto request) {
        log.debug("updating event by id: {}", eventId);
        var event = eventService.updateEvent(eventId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EventMapper.toDto(event));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ResponseEventDto>> searchEvent(@RequestBody RequestSearchEventDto request) {
        log.debug("searching events by dto: {}", request);
        var events = eventService.searchEvent(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events.stream().map(EventMapper::toDto).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<ResponseEventDto>> getMyEvent() {
        log.debug("getting my events");
        var events = eventService.getMyEvent();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events.stream().map(EventMapper::toDto).toList());
    }
}
