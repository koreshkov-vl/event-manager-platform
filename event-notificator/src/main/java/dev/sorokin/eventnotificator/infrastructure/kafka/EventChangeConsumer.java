package dev.sorokin.eventnotificator.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventnotificator.domain.service.NotificationService;
import dev.sorokin.kafka.dto.EventChangeKafkaMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class EventChangeConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "event-changes",
            groupId = "event-notificator"
    )
    public void onEventChange(
            @Payload String rawJson,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) throws JsonProcessingException {

        log.debug("Received: rawJson={}, partition={}, offset={}",
                rawJson, partition, offset);

        EventChangeKafkaMessage message = objectMapper.readValue(rawJson, EventChangeKafkaMessage.class);
        log.debug("EventChangeKafkaMessage={}", message);
        notificationService.processEventChange(message);
        log.debug("message was proceed");
    }
}
