package com.raghvendra;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {

    private final int LIMIT;
    private Map<Integer, Map<K, V>> map = new HashMap<>();
    private int n;
    private int currCount;
    private CacheEvictionPolicy cacheEvictionPolicy;
    private Map<Integer, CacheEvictionPolicy> policyMap;

    public Cache (int n, CacheEvictionPolicy cacheEvictionPolicy, int cacheTotalSize)
            throws IllegalAccessException, InstantiationException {
        LIMIT = cacheTotalSize;
        this.n = n;
        this.currCount = 0;
        this.cacheEvictionPolicy = cacheEvictionPolicy.getClass().newInstance();
        this.policyMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            map.put(i, new HashMap<>());
            policyMap.put(i, cacheEvictionPolicy.getClass().newInstance());
        }
    }

    // This method can be synchronized if multiple threads are accessing it
    private void updateCurrentCount(int val) {
        this.currCount += val;
    }

    public void put(int blockNumber, K key, V value) {
        if (map.keySet().contains(blockNumber)) {
            if (map.get(blockNumber).containsKey(key)) {
                if (map.get(blockNumber).get(key).equals(value)) {
                    return;
                }
            }

            if (this.currCount == LIMIT) {
                int leastRecentlyUsedBlock = (int) this.cacheEvictionPolicy.getTail();
                updateCurrentCount(-1);
                K leastRecentlyUsedKey = (K) policyMap.get(leastRecentlyUsedBlock).getTail();
                map.get(leastRecentlyUsedBlock).remove(leastRecentlyUsedKey);
                if (map.get(leastRecentlyUsedBlock).size() > 0) {
                    this.cacheEvictionPolicy.put(leastRecentlyUsedBlock);
                }

                System.out.println(leastRecentlyUsedBlock + ": " + leastRecentlyUsedKey);
            }

            map.get(blockNumber).put(key, value);
            policyMap.get(blockNumber).put(key);
            this.cacheEvictionPolicy.put(blockNumber);
            updateCurrentCount(1);
        }
    }

    public V get(int blockNumber, K key) {
        if (map.keySet().contains(blockNumber) && map.get(blockNumber).keySet().contains(key)) {
            this.cacheEvictionPolicy.put(blockNumber);
            policyMap.get(blockNumber).put(key);
            return map.get(blockNumber).get(key);
        }

        return null;
    }

    public void delete(int blockNumber, K key) {
        if (map.keySet().contains(blockNumber)) {
            if (map.get(blockNumber).keySet().contains(key)) {
                map.get(blockNumber).remove(key);
                updateCurrentCount(-1);
                policyMap.get(blockNumber).delete(key);

                if (map.get(blockNumber).size() > 0) {
                    this.cacheEvictionPolicy.put(blockNumber);
                }
            }
        }
    }
}

