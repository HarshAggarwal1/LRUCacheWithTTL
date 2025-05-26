package com.harsh.cache;

import com.harsh.item.Item;
import com.harsh.node.Node;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LRUCache implements CacheInterface {

    // static variables
    private static final AtomicLong currentTime = new AtomicLong(0);

    // hits, misses, and evictions
    private final AtomicInteger hits = new AtomicInteger(0);
    private final AtomicInteger misses = new AtomicInteger(0);
    private final AtomicInteger evictions = new AtomicInteger(0);

    // fields
    private final int capacity;
    private final Node head;
    private final Node tail;
    private final ConcurrentHashMap<Integer, Node> cacheMap;

    // executor for removing expired items
    private final ScheduledExecutorService executorService;


    // constructors
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>(capacity);
        this.head = new Node(-1, new Item(-1, -1));
        this.tail = new Node(-1, new Item(-1, -1));

        this.head.setNext(this.tail);
        this.tail.setPrev(this.head);

        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        removeExpiredItems();
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    // getters
    public int getHits() {
        return this.hits.get();
    }
    public int getMisses() {
        return this.misses.get();
    }
    public int getEvictions() {
        return this.evictions.get();
    }

    // methods
    private void addNode(Node node) {
        node.setNext(this.head.getNext());
        node.setPrev(this.head);

        this.head.getNext().setPrev(node);
        this.head.setNext(node);
    }
    private void removeNode(Node node) {
        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
    }
    public synchronized Object getItem(int key) {
        Node node = this.cacheMap.get(key);
        if (node == null) {
            this.misses.getAndIncrement();
            return null;
        }
        else {
            this.hits.getAndIncrement();
            removeNode(node);
            addNode(node);
            return node.getItem().getValue();
        }
    }
    public synchronized void addItem(int key, Object value, long TTL) {
        Node node = this.cacheMap.get(key);
        if (node != null) {
            removeNode(node);
            this.cacheMap.remove(key);
        }
        else {
            if (this.cacheMap.size() >= this.capacity) {
                this.evictions.getAndIncrement();
                Node lastNode = this.tail.getPrev();
                removeNode(lastNode);
                this.cacheMap.remove(lastNode.getKey());
            }
        }
        Item item = new Item(value, LRUCache.currentTime.get() + TTL);
        node = new Node(key, item);

        addNode(node);
        this.cacheMap.put(key, node);
        LRUCache.currentTime.getAndIncrement();
    }
    public synchronized void deleteItem(int key) {
        Node node = this.cacheMap.get(key);
        if (node != null) {
            removeNode(node);
            this.cacheMap.remove(key);
        }
    }
    public synchronized void removeExpiredItems() {
        Node currentNode = this.head.getNext();
        while (currentNode != this.tail) {
            if (currentNode.getItem().getTTL() <= LRUCache.currentTime.get()) {
                Node nextNode = currentNode.getNext();
                removeNode(currentNode);
                this.cacheMap.remove(currentNode.getKey());
                this.evictions.getAndIncrement();
                currentNode = nextNode;
            }
            else {
                currentNode = currentNode.getNext();
            }
        }
    }
    public void shutdown() {
        this.executorService.shutdown();
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        Node currentNode = this.head.getNext();
        while (currentNode != this.tail) {
            sb.append("Key: ").append(currentNode.getKey())
              .append(", Value: ").append(currentNode.getItem().getValue())
              .append(", TTL: ").append(currentNode.getItem().getTTL())
              .append("\n");
            currentNode = currentNode.getNext();
        }
        return sb.toString();
    }

}
