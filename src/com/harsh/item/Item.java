package com.harsh.item;

public class Item {

    // fields
    private final Object value;
    private final long TTL;

    // constructors
    public Item(Object value, long TTL) {
        this.value = value;
        this.TTL = TTL;
    }

    // getters
    public Object getValue() {
        return value;
    }
    public long getTTL() {
        return TTL;
    }

}
