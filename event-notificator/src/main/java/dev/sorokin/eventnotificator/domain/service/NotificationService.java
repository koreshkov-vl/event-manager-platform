package dev.sorokin.eventnotificator.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventnotificator.controller.dto.ChangeItem;
import dev.sorokin.eventnotificator.controller.dto.NotificationPayload;
import dev.sorokin.eventnotificator.controller.dto.UnreadNotificationDto;
import dev.sorokin.eventnotificator.infrastructure.service.UserGetter;
import dev.sorokin.eventnotificator.persistence.entity.NotificationEntity;
import dev.sorokin.eventnotificator.persistence.entity.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.persistence.entity.NotificationEventPayloadJson;
import dev.sorokin.eventnotificator.persistence.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.persistence.repository.NotificationRepository;
import dev.sorokin.kafka.dto.EventChangeKafkaMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationService {

    private final UserGetter userGetter;
    private final NotificationRepository notificationRepository;
    private final NotificationEventPayloadRepository notificationEventPayloadRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void markNotificationsAsRead(@NotEmpty List<Long> ids) {
        var userId = userGetter.getUserIdFromJwt();
        notificationRepository.markAsRead(ids, userId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<UnreadNotificationDto> getUnreadNotifications() {
        var userId = userGetter.getUserIdFromJwt();
        var notifications = notificationRepository.findUnreadByUserIdWithPayload(userId);
        return notifications.stream()
                .map(NotificationService::notificationToDto)
                .toList();
    }

    @Transactional
    public void processEventChange(EventChangeKafkaMessage message) {
        var optionalNotificationEventPayload = notificationEventPayloadRepository.findByMessageId(message.messageId());
        if (optionalNotificationEventPayload.isPresent()) {
            log.warn("Kafka message id = {} already accepted", message.messageId());
            return;
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(
                new NotificationEventPayloadJson(
                    message.eventType(),
                    message.changedById(),
                    message.changes()
                            .stream()
                            .map(item -> new ChangeItem(
                                    item.field(),
                                    item.oldValue(),
                                    item.newValue()
                            )).toList()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot parse message", e);
        }

        NotificationEventPayloadEntity payload = new NotificationEventPayloadEntity(
                null,
            message.messageId(),
            message.eventType(),
            message.eventId(),
            message.occurredAt(),
            message.ownerId(),
            message.changedById(),
            json
        );
        notificationEventPayloadRepository.save(payload);

        List<NotificationEntity> notifications = new ArrayList<>();
        for (Long subscriberId: message.subscribers()) {
            NotificationEntity notification = new NotificationEntity(
                null,
                subscriberId,
                false,
                LocalDateTime.now(),
                null,
                payload
            );
            notifications.add(notification);
        }
        notificationRepository.saveAll(notifications);
    }

    private static UnreadNotificationDto notificationToDto(NotificationEntity entity) {
        NotificationEventPayloadJson payload = null;
        try {
            payload = objectMapper.readValue(entity.getPayload().getPayloadJson(), NotificationEventPayloadJson.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cant parse payload notification", e);
        }

        return new UnreadNotificationDto(
                entity.getId(),
                entity.getPayload().getEventType(),
                entity.getPayload().getEventId(),
                entity.getCreatedAt(),
                entity.isRead(),
                "",
                new NotificationPayload(
                        entity.getPayload().getEventType(),
                        entity.getPayload().getOccurredAt(),
                        entity.getPayload().getChangedById(),
                        entity.getPayload().getOwnerId(),
                        payload.eventName(),
                        payload.changes()
                )
        );
    }
}
