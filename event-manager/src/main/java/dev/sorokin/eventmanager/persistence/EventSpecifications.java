package dev.sorokin.eventmanager.persistence;

import dev.sorokin.eventmanager.persistence.entity.EventEntity;
import dev.sorokin.eventmanager.persistence.entity.EventStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class EventSpecifications {

    private EventSpecifications() {}

    public static Specification<EventEntity> nameEquals(String name) {
        return (root, query, cb) -> name == null ? cb.conjunction() : cb.equal(root.get("name"), name);
    }

    public static Specification<EventEntity> placesMin(Integer placesMin) {
        return (root, query, cb) -> placesMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("maxPlaces"), placesMin);
    }

    public static Specification<EventEntity> placesMax(Integer placesMax) {
        return (root, query, cb) -> placesMax == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("maxPlaces"), placesMax);
    }

    public static Specification<EventEntity> dateStartAfter(OffsetDateTime dateStartAfter) {
        return (root, query, cb) -> dateStartAfter == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("startAt"), dateStartAfter);
    }

    public static Specification<EventEntity> dateStartBefore(OffsetDateTime dateStartBefore) {
        return (root, query, cb) -> dateStartBefore == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("startAt"), dateStartBefore);
    }

    public static Specification<EventEntity> costMin(Integer costMin) {
        return (root, query, cb) -> costMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("cost"), costMin);
    }

    public static Specification<EventEntity> costMax(Integer costMax) {
        return (root, query, cb) -> costMax == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("cost"), costMax);
    }

    public static Specification<EventEntity> durationMin(Integer durationMin) {
        return (root, query, cb) -> durationMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("durationMinutes"), durationMin);
    }

    public static Specification<EventEntity> durationMax(Integer durationMax) {
        return (root, query, cb) -> durationMax == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("durationMinutes"), durationMax);
    }

    public static Specification<EventEntity> locationIdEquals(Long locationId) {
        return (root, query, cb) -> locationId == null ? cb.conjunction() : cb.equal(root.get("locationId"), locationId);
    }

    public static Specification<EventEntity> statusEquals(EventStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
