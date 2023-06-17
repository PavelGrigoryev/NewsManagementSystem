package ru.clevertec.newsservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class RedisCacheConfigTest {

    @Spy
    private RedisCacheConfig redisCacheConfig;

    @Test
    @DisplayName("test should return not null RedisCacheConfiguration")
    void testShouldReturnNotNullRedisCacheConfiguration() {
        RedisCacheConfiguration configuration = redisCacheConfig.redisCacheConfiguration();

        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("test should return RedisCacheConfiguration with expected params")
    void testShouldReturnRedisCacheConfigurationWithExpectedParams() {
        RedisCacheConfiguration configuration = redisCacheConfig.redisCacheConfiguration();

        assertAll(
                () -> assertThat(configuration.getAllowCacheNullValues()).isFalse(),
                () -> assertThat(configuration.getTtl()).isEqualTo(Duration.ofMinutes(30))
        );
    }

}
