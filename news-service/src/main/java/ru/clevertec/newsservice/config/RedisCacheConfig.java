package ru.clevertec.newsservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

/**
 * This class provides configuration for Redis caching in production mode.
 */
@Configuration
@EnableCaching
@Profile(value = "prod")
public class RedisCacheConfig {

    /**
     * Configures the Redis cache with a default time-to-live of 30 minutes and disables caching of null values.
     *
     * @return {@link RedisCacheConfiguration}.
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(30));
    }

}
