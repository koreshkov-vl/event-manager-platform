package dev.sorokin.eventnotificator.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class ErrorHandlerConfiguration {

    @Value("${spring.kafka.dlt.retry-interval-ms:1000}")
    private long retryIntervalMs;

    @Value("${spring.kafka.dlt.max-attempts:3}")
    private long maxAttempts;

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        var backOff = new FixedBackOff(retryIntervalMs, maxAttempts);

        var handler = new DefaultErrorHandler(recoverer, backOff);
        handler.setCommitRecovered(true);
        return handler;
    }
}
