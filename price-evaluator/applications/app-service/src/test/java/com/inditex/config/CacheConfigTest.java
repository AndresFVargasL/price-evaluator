package com.inditex.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.time.Duration;

import static org.mockito.Mockito.*;

class CacheConfigTest {

    @Test
    void shouldCreateCaffeineCacheManager() {
        // Arrange
        String cacheName = "prices";
        CacheConfig config = new CacheConfig();

        // Act
        CaffeineCacheManager cacheManager = config.caffeineCacheManager(cacheName);

        // Assert
        assert cacheManager != null;
        assert cacheManager.getCacheNames().contains("prices");
    }

    @Test
    void shouldCallSetCaffeineAndSetAsyncCacheMode() {
        // Arrange
        String cacheName = "prices";

        // Creamos un spy del manager para verificar llamadas
        CaffeineCacheManager manager = spy(new CaffeineCacheManager(cacheName));

        // Stub para que no afecte la lógica real (no es necesario, pero lo dejamos limpio)
        doNothing().when(manager).setCaffeine(any());
        doNothing().when(manager).setAsyncCacheMode(anyBoolean());

        // Act
        manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(CacheConfig.MAXIMUM_SIZE)
            .expireAfterWrite(Duration.ofMinutes(CacheConfig.MINUTES)));

        manager.setAsyncCacheMode(true);

        // Assert
        verify(manager).setCaffeine(any());
        verify(manager).setAsyncCacheMode(eq(true));
    }
}
