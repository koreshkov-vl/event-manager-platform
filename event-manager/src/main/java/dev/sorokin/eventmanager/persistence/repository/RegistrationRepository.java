package dev.sorokin.eventmanager.persistence.repository;

import dev.sorokin.eventmanager.persistence.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);
}
