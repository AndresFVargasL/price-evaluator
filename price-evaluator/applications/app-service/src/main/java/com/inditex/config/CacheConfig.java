package com.inditex.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    public static final int MAXIMUM_SIZE = 1000;
    public static final int MINUTES = 10;

    @Bean
    public CaffeineCacheManager caffeineCacheManager(@Value("${spring.cache.cache-names}") String cacheName) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheName);
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(java.time.Duration.ofMinutes(MINUTES)));

        cacheManager.setAsyncCacheMode(true);

        return cacheManager;
    }
}

