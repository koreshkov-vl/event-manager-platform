package dev.sorokin.eventmanager.infrastructure.service;

import dev.sorokin.eventmanager.domain.service.GetterUserService;
import dev.sorokin.eventmanager.infrastructure.outbox.OutboxService;
import dev.sorokin.kafka.dto.ChangeItem;
import dev.sorokin.kafka.dto.EventChangeKafkaMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class EventOutboxSaver {

    private final static String TOPIC = "event-changes";
    private final static String EVENT_STATUS = "EVENT_UPDATED";

    private final GetterUserService getterUserService;
    private final OutboxService outboxService;

    public EventOutboxSaver(GetterUserService getterUserService, OutboxService outboxService) {
            this.getterUserService = getterUserService;
            this.outboxService = outboxService;
    }

    @Transactional
    public void save(Long eventId, Long eventUserId, List<Long> subscribers, List<ChangeItem> changes) {
        var userId = getterUserService.getUserIdFromContext().isPresent() ? eventUserId : null;
        outboxService.save(
                TOPIC,
                String.valueOf(eventId),
                new EventChangeKafkaMessage(
                        UUID.randomUUID(),
                        EVENT_STATUS,
                        eventId,
                        LocalDateTime.now(),
                        eventUserId,
                        userId,
                        subscribers,
                        changes
                )
        );
    }
}
