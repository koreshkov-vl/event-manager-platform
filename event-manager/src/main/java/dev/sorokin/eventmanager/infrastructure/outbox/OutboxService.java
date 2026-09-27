package dev.sorokin.eventmanager.infrastructure.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void save(String topic, String key, Object payload) {
        try {
            OutboxMessage message = new OutboxMessage();
            message.setMessageId(UUID.randomUUID());
            message.setTopic(topic);
            message.setMessageKey(key);
            message.setPayload(objectMapper.writeValueAsString(payload));
            message.setCreatedAt(OffsetDateTime.now());
            message.setProcessedAt(null);
            message.setAttempts(0);
            outboxRepository.save(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize outbox payload", e);
        }
    }
}
