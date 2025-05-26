package com.harsh.application;

import com.harsh.cache.LRUCache;
import com.harsh.cache.CacheInterface;

public class Cache {

    // fields
    private final CacheInterface cache;

    // Constructor
    public Cache(CacheInterface cache) {
        this.cache = cache;
    }

    // methods
    public Object getItem(int key) {
        return cache.getItem(key);
    }
    public void addItem(int key, Object value, long TTL) {
        cache.addItem(key, value, TTL);
    }
    public void deleteItem(int key) {
        cache.deleteItem(key);
    }

    public int getHits() {
        if (cache instanceof LRUCache) {
            return ((LRUCache) cache).getHits();
        }
        return 0;
    }
    public int getMisses() {
        if (cache instanceof LRUCache) {
            return ((LRUCache) cache).getMisses();
        }
        return 0;
    }
    public int getEvictions() {
        if (cache instanceof LRUCache) {
            return ((LRUCache) cache).getEvictions();
        }
        return 0;
    }

    public void shutdown() {
        if (cache instanceof LRUCache) {
            ((LRUCache) cache).shutdown();
        }
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cache=" + cache +
                '}';
    }
}
