package dev.sorokin.eventmanager.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class LoggingCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) {
        log.error("Cache GET error: cache={}, key={}", cache.getName(), key, ex);
    }

    @Override
    public void handleCachePutError(RuntimeException ex, Cache cache, Object key, Object value) {
        log.error("Cache PUT error: cache={}, key={}", cache.getName(), key, ex);
    }

    @Override
    public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) {
        log.error("Cache EVICT error: cache={}, key={}", cache.getName(), key, ex);
    }

    @Override
    public void handleCacheClearError(RuntimeException ex, Cache cache) {
        log.error("Cache CLEAR error: cache={}", cache.getName(), ex);
    }
}
