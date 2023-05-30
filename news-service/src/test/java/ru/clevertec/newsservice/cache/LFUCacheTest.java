package ru.clevertec.newsservice.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.util.testbuilder.news.NewsResponseTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    private Cache<Long, NewsResponse> cache;
    private static final NewsResponseTestBuilder TEST_BUILDER = NewsResponseTestBuilder.aNewsResponse();

    @BeforeEach
    void setUp() {
        cache = new LFUCache<>(3);
        cache.put(1L, TEST_BUILDER.build());
        cache.put(2L, TEST_BUILDER.withId(2L)
                .withTitle("All stand together")
                .withText("Go go go go go go")
                .build());
        cache.put(3L, TEST_BUILDER.withId(3L)
                .withTitle("Лето пришло")
                .withText("На заславском водохранилище утонуло 10 человек")
                .build());
    }

    @Test
    @DisplayName("test get method should return value by key")
    void testGetMethodShouldReturnValueByKey() {
        NewsResponse expectedValue = TEST_BUILDER.build();

        NewsResponse actualValue = cache.get(1L);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test get method should return null when cache does not contain a value by key")
    void testGetMethodShouldReturnNull() {
        NewsResponse actualValue = cache.get(4L);

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("test expected key should be removed from all maps")
    void testExpectedKeyShouldBeRemovedFromAllMaps() {
        NewsResponse expectedValue = TEST_BUILDER.withId(4L).build();
        cache.put(4L, expectedValue);

        assertThat(cache.get(1L)).isNull();
        assertThat(cache.get(4L)).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test cache should remove value with less frequency")
    void testCacheShouldRemoveValueWithLessFrequency() {
        cache.get(1L);
        cache.get(2L);
        cache.put(4L, TEST_BUILDER.withId(4L).build());

        assertThat(cache.get(3L)).isNull();

        cache.put(5L, TEST_BUILDER.withId(5L).build());

        assertThat(cache.get(4L)).isNull();
    }

    @Test
    @DisplayName("test put method should return deleted value if cache contains expected key")
    void testPutMethodShouldReturnDeletedValueIfCacheContainsExpectedKey() {
        NewsResponse expectedValue = TEST_BUILDER.build();

        NewsResponse actualValue = cache.put(1L, TEST_BUILDER.withId(5L).build());

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test put method should return null if capacity of cache <= 0")
    void testPutMethodShouldReturnNullIfCapacityIsLessOrEqualZero() {
        cache = new LFUCache<>(0);
        NewsResponse actualValue = cache.put(1L, TEST_BUILDER.build());

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("test that minFrequency in get method is incremented by 1")
    void testMinFrequencyShouldIncrementInGetMethod() {
        cache.get(1L);
        cache.get(2L);
        cache.get(3L);

        assertThat(cache.toString()).endsWith("=2)");
    }

    @Test
    @DisplayName("test toString method starts with expected value")
    void testToStringMethodStartsWithExpectedValue() {
        String actualValue = cache.toString();

        assertThat(actualValue).startsWith("LFUCache(capacity=3");
    }

    @Test
    @DisplayName("test removeByKey method should return removed value")
    void testRemoveByKeyShouldReturnRemovedValue() {
        NewsResponse expectedValue = TEST_BUILDER.build();

        NewsResponse actualValue = cache.removeByKey(1L);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test removeByKey method should return null when cache does not contain a value by key")
    void testRemoveByKeyMethodShouldReturnNull() {
        NewsResponse actualValue = cache.removeByKey(4L);

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("test that minFrequency in removeByKey method is incremented by 1")
    void testMinFrequencyShouldIncrementInRemoveByKeyMethod() {
        cache.removeByKey(1L);
        cache.removeByKey(2L);
        cache.removeByKey(3L);

        assertThat(cache.toString()).endsWith("=2)");
    }

}
