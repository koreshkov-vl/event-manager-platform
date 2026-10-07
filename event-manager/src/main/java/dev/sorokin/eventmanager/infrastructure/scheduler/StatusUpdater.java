package dev.sorokin.eventmanager.infrastructure.scheduler;

import dev.sorokin.eventmanager.infrastructure.service.EventOutboxSaver;
import dev.sorokin.eventmanager.persistence.entity.EventStatus;
import dev.sorokin.eventmanager.persistence.repository.EventRepository;
import dev.sorokin.eventmanager.persistence.repository.RegistrationRepository;
import dev.sorokin.kafka.dto.ChangeItem;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
public class StatusUpdater {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventOutboxSaver eventOutboxSaver;
    private final CacheManager cacheManager;

    public StatusUpdater(
            EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            EventOutboxSaver eventOutboxSaver,
            CacheManager cacheManager) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.eventOutboxSaver = eventOutboxSaver;
        this.cacheManager = cacheManager;
    }

    @Scheduled(cron = "${event.status.cron}")
    @Transactional
    public void update() {
        log.debug("start updating...");

        var eventsStarted = eventRepository.findEventsToStart(EventStatus.WAIT_START, OffsetDateTime.now());

        var cache = cacheManager.getCache("events");

        if (!eventsStarted.isEmpty()) {
            int cnt = eventRepository.startEvents(
                    EventStatus.WAIT_START,
                    EventStatus.STARTED,
                    OffsetDateTime.now());
            if (cnt > 0) {
                log.debug("events = " + cnt + ", were updated to 'STARTED'");
            }

            List<ChangeItem> changes = List.of(
                    new ChangeItem("status", EventStatus.WAIT_START, EventStatus.STARTED));
            eventsStarted.forEach(
                 event ->  {
                     if (cache != null) {
                         try {
                             cache.evict("id:" + event.getId());
                         } catch (Exception e) {
                             log.warn("Failed to evict cache for event {}", event.getId(), e);
                         }
                     }
                     var subscribers = getSubscribers(event.getId());
                     if (!subscribers.isEmpty()) {
                         eventOutboxSaver.save(
                                 event.getId(),
                                 event.getUserId(),
                                 subscribers,
                                 changes
                         );
                     }
                 }
            );
        }

        var eventsFinished = eventRepository.findFinishEvents(EventStatus.STARTED.toString(), OffsetDateTime.now());
        if (!eventsFinished.isEmpty()) {
            int cnt = eventRepository.finishEvents(
                    EventStatus.STARTED.toString(),
                    EventStatus.FINISHED.toString(),
                    OffsetDateTime.now());
            if (cnt > 0) {
                log.debug("events = " + cnt + ", were updated to 'FINISHED'");
            }

            List<ChangeItem> changes = List.of(
                    new ChangeItem("status", EventStatus.STARTED, EventStatus.FINISHED));

            eventsFinished.forEach(
                    event ->  {
                        if (cache != null) {
                            try {
                                cache.evict("id:" + event.getId());
                            } catch (Exception e) {
                                log.warn("Failed to evict cache for event {}", event.getId(), e);
                            }
                        }
                        var subscribers = getSubscribers(event.getId());
                        if (!subscribers.isEmpty()) {
                            eventOutboxSaver.save(
                                    event.getId(),
                                    event.getUserId(),
                                    subscribers,
                                    changes
                            );
                        }
                    }
            );
        }
    }

    private List<Long> getSubscribers(Long eventId) {
        return registrationRepository.findUserIdsByEventId(eventId);
    }
}
