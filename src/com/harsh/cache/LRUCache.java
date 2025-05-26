package com.harsh.cache;

import com.harsh.item.Item;
import com.harsh.node.Node;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LRUCache implements CacheInterface {

    // static variables
    private static long currentTime = 0;

    // hits, misses, and evictions
    private volatile int hits = 0;
    private volatile int misses = 0;
    private volatile int evictions = 0;

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

        head.setNext(tail);
        tail.setPrev(head);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(
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
    public synchronized int getHits() {
        return hits;
    }
    public synchronized int getMisses() {
        return misses;
    }
    public synchronized int getEvictions() {
        return evictions;
    }

    // methods
    private void addNode(Node node) {
        node.setNext(head.getNext());
        node.setPrev(head);

        head.getNext().setPrev(node);
        head.setNext(node);
    }
    private void removeNode(Node node) {
        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
    }
    public synchronized Object getItem(int key) {
        Node node = cacheMap.get(key);
        if (node == null) {
            misses++;
            return null;
        }
        else {
            hits++;
            removeNode(node);
            addNode(node);
            return node.getItem().getValue();
        }
    }
    public synchronized void addItem(int key, Object value, long TTL) {
        LRUCache.currentTime++;

        Node node = cacheMap.get(key);
        if (node != null) {
            removeNode(node);
            cacheMap.remove(key);
        }
        else {
            if (cacheMap.size() >= capacity) {
                evictions++;
                Node lastNode = tail.getPrev();
                removeNode(lastNode);
                cacheMap.remove(lastNode.getKey());
            }
        }
        Item item = new Item(value, LRUCache.currentTime + TTL);
        node = new Node(key, item);

        addNode(node);
        cacheMap.put(key, node);
    }
    public synchronized void deleteItem(int key) {
        Node node = cacheMap.get(key);
        if (node != null) {
            removeNode(node);
            cacheMap.remove(key);
        }
    }
    public synchronized void removeExpiredItems() {
        Node currentNode = head.getNext();
        while (currentNode != tail) {
            if (currentNode.getItem().getTTL() <= LRUCache.currentTime) {
                Node nextNode = currentNode.getNext();
                removeNode(currentNode);
                cacheMap.remove(currentNode.getKey());
                evictions++;
                currentNode = nextNode;
            }
            else {
                currentNode = currentNode.getNext();
            }
        }
    }
    public void shutdown() {
        executorService.shutdown();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node currentNode = head.getNext();
        while (currentNode != tail) {
            sb.append("Key: ").append(currentNode.getKey())
              .append(", Value: ").append(currentNode.getItem().getValue())
              .append(", TTL: ").append(currentNode.getItem().getTTL())
              .append("\n");
            currentNode = currentNode.getNext();
        }
        return sb.toString();
    }

}
