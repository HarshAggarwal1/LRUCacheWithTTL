🔁 LRU Cache with TTL and Metrics (Multithreaded)
This project implements a thread-safe LRU (Least Recently Used) cache in Java with the following features:

✅ TTL (Time-To-Live) support for automatic expiration of items

✅ Eviction policy based on both capacity and TTL

✅ Access metrics: hits, misses, and evictions tracked using atomic counters

✅ Doubly linked list for efficient LRU ordering

✅ Multithreaded support with synchronized operations and background cleanup thread

✅ Scheduled executor to periodically remove expired entries

🧪 Demo
The CacheDemo class simulates concurrent cache usage by spawning multiple threads for:

Adding and accessing items

Forcing evictions

Handling expired entries

Printing real-time cache state and metrics

This is ideal for learning and demonstrating in-memory caching behavior under concurrent access.
