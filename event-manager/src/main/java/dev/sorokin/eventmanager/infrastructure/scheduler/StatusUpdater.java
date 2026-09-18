package dev.sorokin.eventmanager.infrastructure.scheduler;

import dev.sorokin.eventmanager.persistence.entity.EventStatus;
import dev.sorokin.eventmanager.persistence.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@Slf4j
public class StatusUpdater {

    private final EventRepository eventRepository;

    public StatusUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.status.cron}")
    @Transactional
    public void update() {
        log.debug("start updating...");

        int eventsStarted = eventRepository.startEvents(
                EventStatus.WAIT_START,
                EventStatus.STARTED,
                OffsetDateTime.now());
        if (eventsStarted > 0) {
            log.debug("events = " + eventsStarted + ", were updated to 'STARTED'");
        }

        int eventsFinished = eventRepository.finishEvents(
                EventStatus.STARTED.toString(),
                EventStatus.FINISHED.toString(),
                OffsetDateTime.now());
        if (eventsFinished > 0) {
            log.debug("events = " + eventsStarted + ", were updated to 'FINISHED'");
        }
    }
}
