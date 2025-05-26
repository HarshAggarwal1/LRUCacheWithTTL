package com.harsh.cache;

public interface CacheInterface {

    Object getItem(int key);
    void addItem(int key, Object value, long TTL);
    void deleteItem(int key);

}
