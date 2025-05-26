package com.harsh.cache;

public interface CacheInterface {

    public Object getItem(int key);
    public void addItem(int key, Object value, long TTL);
    public void deleteItem(int key);

}
