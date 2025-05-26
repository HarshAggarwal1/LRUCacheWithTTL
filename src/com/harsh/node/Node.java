package com.harsh.node;

import com.harsh.item.Item;

public class Node {

    // fields
    private final int key;
    private final Item item;
    private Node next;
    private Node prev;

    // constructors
    public Node(int key, Item item) {
        this.key = key;
        this.item = item;
        this.next = null;
        this.prev = null;
    }

    // getters
    public int getKey() {
        return this.key;
    }
    public Item getItem() {
        return this.item;
    }
    public Node getNext() {
        return this.next;
    }
    public Node getPrev() {
        return this.prev;
    }

    // setters
    public void setNext(Node next) {
        this.next = next;
    }
    public void setPrev(Node prev) {
        this.prev = prev;
    }

}
