package dev.sorokin.eventmanager.infrastructure.outbox.scheduler;

import dev.sorokin.eventmanager.infrastructure.kafka.KafkaProducer;
import dev.sorokin.eventmanager.infrastructure.outbox.OutboxMessage;
import dev.sorokin.eventmanager.infrastructure.outbox.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final int BATCH_SIZE = 50;
    private static final int MAX_ATTEMPTS = 5;

    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaEventPublisher;

    @Scheduled(cron = "${outbox.cron}")
    @Transactional
    public void publish() {
        List<OutboxMessage> messages = outboxRepository.findUnprocessedForUpdate(BATCH_SIZE, MAX_ATTEMPTS);

        if (messages.isEmpty()) {
            return;
        }

        for (OutboxMessage message : messages) {
            if (message.getAttempts() >= MAX_ATTEMPTS) {
                log.warn("Outbox: message {} exceeded max attempts", message.getId());
                continue;
            }

            try {
                kafkaEventPublisher.publish(
                        message.getTopic(),
                        message.getMessageKey(),
                        message.getPayload()
                );
                message.setProcessedAt(OffsetDateTime.now());
                message.setLastError(null);
            } catch (Exception e) {
                log.error("Outbox: failed to publish message id={}", message.getId(), e);
                message.setAttempts(message.getAttempts() + 1);
                message.setLastError(truncate(e.getMessage(), 1000));
            }
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
