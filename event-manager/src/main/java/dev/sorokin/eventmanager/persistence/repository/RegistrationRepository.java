package dev.sorokin.eventmanager.persistence.repository;

import dev.sorokin.eventmanager.persistence.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    @Query(value = "SELECT user_id FROM registrations WHERE event_id = :eventId", nativeQuery = true)
    List<Long> findUserIdsByEventId(@Param("eventId") Long eventId);
}
