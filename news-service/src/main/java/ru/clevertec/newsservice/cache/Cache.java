package ru.clevertec.newsservice.cache;

public interface Cache<K, V> {

    V get(K key);

    V put (K key, V value);

    V removeByKey(K key);

}
