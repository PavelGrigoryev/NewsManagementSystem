package ru.clevertec.newsservice.integration.cache.factory;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.cache.LFUCache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CacheFactoryImplTest extends BaseIntegrationTest {

    @Value("${cache.capacity}")
    private Integer cacheCapacity;

    private final CacheFactory<String, String> cacheFactory;

    @Test
    @DisplayName("test cacheFactory should create newsCache")
    void testShouldCreateNewsCache() {
        Cache<String, String> newsCache = cacheFactory.createNewsCache();

        assertThat(newsCache).isNotNull();
    }

    @Test
    @DisplayName("test cacheFactory should create commentCache")
    void testShouldCreateCommentCache() {
        Cache<String, String> commentCache = cacheFactory.createCommentCache();

        assertThat(commentCache).isNotNull();
    }

    @Test
    @DisplayName("test cacheFactory should create instanceOf LFUCache")
    void testShouldCreateInstanceOfLFUCache() {
        Cache<String, String> newsCache = cacheFactory.createNewsCache();

        assertThat(newsCache).isInstanceOf(LFUCache.class);
    }

    @Test
    @DisplayName("test cacheCapacity should be 5")
    void testCacheCapacityShouldBeFive() {
        assertThat(cacheCapacity).isEqualTo(5);
    }

}
