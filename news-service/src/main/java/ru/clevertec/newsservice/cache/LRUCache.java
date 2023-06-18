package ru.clevertec.newsservice.cache;

import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An implementation of the Cache interface that uses the Least Recently Used (LRU) algorithm
 * to evict entries when the cache reaches its capacity.
 *
 * @param <K> the type of keys maintained by this cache.
 * @param <V> the type of mapped values.
 */
@EqualsAndHashCode(callSuper = false)
public class LRUCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    private final int capacity;

    /**
     * Constructs a new LRUCache with the specified capacity.
     *
     * @param capacity the maximum number of entries that this cache can hold.
     */
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Removes the eldest entry from the cache when it reaches its capacity.
     *
     * @param eldest the least recently used entry in the map.
     * @return true if the eldest entry should be removed from the map, false otherwise.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.capacity;
    }

    /**
     * Removes the entry for the specified key from this cache if present.
     * Returns the value to which this cache previously associated the key,
     * or null if the cache contained no mapping for the key.
     *
     * @param key the key whose mapping is to be removed from the cache.
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public V removeByKey(K key) {
        return remove(key);
    }

}
