package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.controller.dto.ResponseEventDto;
import dev.sorokin.eventmanager.domain.service.RegistrationService;
import dev.sorokin.eventmanager.mapper.EventMapper;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> createRegistration(@PathVariable @NotNull Long eventId) {
        log.debug("registration on event id: {}", eventId);
        registrationService.createRegistration(eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable @NotNull Long eventId) {
        log.debug("cancel registration on event id: {}", eventId);
        registrationService.cancelRegistration(eventId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<ResponseEventDto>> getMyRegistration() {
        log.debug("getting my events");
        var events = registrationService.getMyRegistrations();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events.stream().map(EventMapper::toDto).toList());
    }
}
