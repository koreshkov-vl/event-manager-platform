package dev.sorokin.eventmanager.persistence.repository;

import dev.sorokin.eventmanager.persistence.entity.EventEntity;
import dev.sorokin.eventmanager.persistence.entity.EventStatus;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> findByIdWithLock(Long id);

    @Query("SELECT DISTINCT e FROM EventEntity e " +
            "JOIN FETCH e.location " +
            "JOIN e.registrations r WHERE r.user.id = :userId")
    List<EventEntity> findEventRegistrationsByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE EventEntity e SET e.status = :newStatus " +
            "WHERE e.status = :oldStatus AND e.startAt <= :now")
    int startEvents(
            @Param("oldStatus") EventStatus oldStatus,
            @Param("newStatus") EventStatus newStatus,
            @Param("now") OffsetDateTime now
    );

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                UPDATE events 
                SET status = :newStatus
                WHERE status = :oldStatus
                AND start_at + (duration_minutes * INTERVAL '1 minute') <= :now
            """,
            nativeQuery = true
    )
    int finishEvents(
            @Param("oldStatus") String oldStatus,
            @Param("newStatus") String newStatus,
            @Param("now") OffsetDateTime now
    );
}

