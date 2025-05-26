package com.harsh.application;

import com.harsh.cache.LRUCache;

public class CacheDemo {
    public static void run() throws InterruptedException {
        // Create an LRU cache with capacity 3
        Cache cache = new Cache(new LRUCache(3));

        System.out.println("Adding items A, B, and C with TTL = 2 seconds");
        cache.addItem(1, "A", 2);
        cache.addItem(2, "B", 2);
        cache.addItem(3, "C", 2);

        // Access some items
        System.out.println("Get key 2: " + cache.getItem(2)); // hit B
        System.out.println("Get key 4 (missing): " + cache.getItem(4)); // miss

        // Show metrics so far
        System.out.println(cache);
        System.out.println("Hits: " + cache.getHits() + ", Misses: " + cache.getMisses() + ", Evictions: " + cache.getEvictions());

        // Add another item to force eviction by capacity
        System.out.println("Adding item D (key=4) to exceed capacity");
        cache.addItem(4, "D", 2);
        System.out.println("Get key 1 (should be evicted): " + cache.getItem(1));

        // Show metrics after eviction
        System.out.println(cache);
        System.out.println("Hits: " + cache.getHits() + ", Misses: " + cache.getMisses() + ", Evictions: " + cache.getEvictions());

        // Wait for TTL to expire
        System.out.println("Waiting 3 seconds for TTL expiration...");
        Thread.sleep(3000);

        // Trigger cleanup
        System.out.println("Get key 2 after TTL expiry: " + cache.getItem(2)); // should be null
        System.out.println("Get key 3 after TTL expiry: " + cache.getItem(3)); // should be null

        // Show final metrics
        System.out.println(cache);
        System.out.println("Hits: " + cache.getHits() + ", Misses: " + cache.getMisses() + ", Evictions: " + cache.getEvictions());

        // Shutdown the scheduler
        cache.shutdown();
        System.out.println("Demo complete.");
    }
}
