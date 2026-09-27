package dev.sorokin.eventnotificator.persistence.repository;

import dev.sorokin.eventnotificator.persistence.entity.NotificationEventPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {

    Optional<NotificationEventPayloadEntity> findByMessageId(UUID messageId);
}
