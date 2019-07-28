package com.raghvendra;

public interface CacheEvictionPolicy<K> {
    void put(K key);
    void delete(K key);
    K getTail();
}
