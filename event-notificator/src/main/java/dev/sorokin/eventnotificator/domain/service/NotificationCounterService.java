package dev.sorokin.eventnotificator.domain.service;

import dev.sorokin.eventnotificator.persistence.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationCounterService {

    private static final String KEY = "notification:unread:";

    private final StringRedisTemplate stringRedisTemplate;
    private final NotificationRepository notificationRepository;

    public void incUnread(Long userId, Long increment) {
        stringRedisTemplate.opsForValue().increment(key(userId), increment);
    }

    public void updateRead(Long userId) {
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);
        stringRedisTemplate.opsForValue().set(
                key(userId),
                Long.toString(unreadCount)
        );
    }

    private String key(Long userId) {
        return KEY + userId;
    }
}