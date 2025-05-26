package com.harsh.application;

import com.harsh.cache.LRUCache;

public class CacheDemo {
    public static void run() throws InterruptedException {
        // Create an LRU cache with capacity 3
        Cache cache = new Cache(new LRUCache(3));

        System.out.println("Adding items A, B, and C with TTL = 2 seconds (done by Thread 1)");
        Thread t1 = new Thread(() -> {
            cache.addItem(1, "A", 2);
            cache.addItem(2, "B", 2);
            cache.addItem(3, "C", 2);
        });

        Thread t2 = new Thread(() -> {
            System.out.println("Thread 2: Get key 2 -> " + cache.getItem(2)); // hit B
            System.out.println("Thread 2: Get key 4 (missing) -> " + cache.getItem(4)); // miss
        });

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(500); // wait to let earlier items be inserted
                System.out.println("Thread 3: Adding item D (key=4) to exceed capacity");
                cache.addItem(4, "D", 2);
                System.out.println("Thread 3: Get key 1 (should be evicted) -> " + cache.getItem(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start threads in sequence with a slight delay
        t1.start();
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();

        // Print current state and metrics
        System.out.println("\nCache after multithreaded operations:");
        System.out.println(cache);
        System.out.println("Hits: " + cache.getHits() + ", Misses: " + cache.getMisses() + ", Evictions: " + cache.getEvictions());

        // Wait for TTL expiration
        System.out.println("\nWaiting 3 seconds for TTL expiration...");
        Thread.sleep(3000);

        // Threads to test expired item access
        Thread t4 = new Thread(() -> {
            System.out.println("Thread 4: Get key 2 after TTL expiry -> " + cache.getItem(2));
            System.out.println("Thread 4: Get key 3 after TTL expiry -> " + cache.getItem(3));
        });

        t4.start();
        t4.join();

        // Final output
        System.out.println("\nFinal cache state:");
        System.out.println(cache);
        System.out.println("Hits: " + cache.getHits() + ", Misses: " + cache.getMisses() + ", Evictions: " + cache.getEvictions());

        cache.shutdown();
        System.out.println("Demo complete.");
    }
}
