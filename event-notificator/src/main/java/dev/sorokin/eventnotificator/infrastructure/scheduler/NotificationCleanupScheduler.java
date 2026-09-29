package dev.sorokin.eventnotificator.infrastructure.scheduler;

import dev.sorokin.eventnotificator.persistence.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "notification.cleanup.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class NotificationCleanupScheduler {

    private final NotificationRepository notificationRepository;

    public NotificationCleanupScheduler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Value("${notification.cleanup.retention-days:7}")
    private int retentionDays;

    @Transactional
    @Scheduled(cron = "${notification.cleanup.cron:0 0 3 * * *}")
    public void cleanup() {

        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);

        int deleted = notificationRepository.deleteReadOlderThan(cutoff);

        if (deleted > 0) {
            log.info("Cleanup: deleted {} read notifications older than {} days",
                    deleted, retentionDays);
        } else {
            log.debug("Cleanup: nothing to delete");
        }
    }
}
