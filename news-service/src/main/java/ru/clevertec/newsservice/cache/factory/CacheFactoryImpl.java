package ru.clevertec.newsservice.cache.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.cache.LFUCache;
import ru.clevertec.newsservice.cache.LRUCache;

/**
 * The CacheFactoryImpl class that implements CacheFactory interface and produces a cache depending on the parameters
 * in the application.yaml file.
 */
@Configuration
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer capacity;

    @Bean
    @Override
    public Cache<K, V> createNewsCache() {
        return getCache();
    }

    @Bean
    @Override
    public Cache<K, V> createCommentCache() {
        return getCache();
    }

    private Cache<K, V> getCache() {
        return "LRU".equalsIgnoreCase(algorithm)
                ? new LRUCache<>(capacity)
                : new LFUCache<>(capacity);
    }

}
