package dev.sorokin.eventmanager.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String topic, String key, String jsonPayload) {
        try {
            kafkaTemplate.send(topic, key, jsonPayload).get(3, TimeUnit.SECONDS);
            log.debug("Sent to kafka: topic={}, key={}", topic, key);
        } catch (Exception e) {
            log.error("Failed to send to kafka: topic={}, key={}", topic, key, e);
            throw new RuntimeException(e);
        }
    }
}
