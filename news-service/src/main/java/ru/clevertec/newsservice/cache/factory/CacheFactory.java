package ru.clevertec.newsservice.cache.factory;

import ru.clevertec.newsservice.cache.Cache;

public interface CacheFactory<K, V> {

    Cache<K, V> createNewsCache();

    Cache<K, V> createCommentCache();

}
