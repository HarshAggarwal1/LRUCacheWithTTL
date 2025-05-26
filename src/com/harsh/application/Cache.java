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
        return this.cache.getItem(key);
    }
    public void addItem(int key, Object value, long TTL) {
        this.cache.addItem(key, value, TTL);
    }
    public void deleteItem(int key) {
        this.cache.deleteItem(key);
    }

    public int getHits() {
        if (this.cache instanceof LRUCache) {
            return ((LRUCache) this.cache).getHits();
        }
        return 0;
    }
    public int getMisses() {
        if (this.cache instanceof LRUCache) {
            return ((LRUCache) this.cache).getMisses();
        }
        return 0;
    }
    public int getEvictions() {
        if (this.cache instanceof LRUCache) {
            return ((LRUCache) this.cache).getEvictions();
        }
        return 0;
    }

    public void shutdown() {
        if (this.cache instanceof LRUCache) {
            ((LRUCache) this.cache).shutdown();
        }
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cache=" + this.cache +
                '}';
    }
}
