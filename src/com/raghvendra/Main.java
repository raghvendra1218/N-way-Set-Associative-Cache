package com.raghvendra;

public class Main {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Cache cache = new Cache(4, new LruCache(), 2);
        cache.put(0, 1, 1);
        cache.put(1, 2, 2);
        cache.put(1, 3, 3);

        System.out.println(cache.get(1, 2));

        cache.put(3, 4, 4);
        cache.put(3, 5, 5);
        cache.put(3, 2, 1);

    }
}
