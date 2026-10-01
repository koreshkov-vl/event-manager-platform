package dev.sorokin.eventnotificator.persistence.repository;

import dev.sorokin.eventnotificator.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true, n.readAt = :readAt WHERE n.userId = :userId AND n.isRead = false AND n.id IN :ids")
    int markAsRead(
            @Param("ids") List<Long> ids,
            @Param("userId") Long userId,
            @Param("readAt") LocalDateTime readAt);

    @Query("SELECT n FROM NotificationEntity n JOIN FETCH n.payload WHERE n.isRead = false AND n.userId = :userId ORDER BY n.createdAt DESC")
    List<NotificationEntity> findUnreadByUserIdWithPayload(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.isRead = true AND n.readAt < :cutoff")
    int deleteReadOlderThan(@Param("cutoff") LocalDateTime cutoff);

    long countByUserIdAndIsReadFalse(@Param("userId") Long userId);
}
