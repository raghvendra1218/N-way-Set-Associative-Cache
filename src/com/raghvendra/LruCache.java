package com.raghvendra;

import java.util.HashMap;
import java.util.Map;

public class LruCache<K> implements CacheEvictionPolicy {
    private Map<K, Node> cache;
    private Node head;
    private Node tail;

    public LruCache () {
        cache = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void put (Object key) {
        Node node = cache.getOrDefault((K) key, null);

        if (node == null) {
            Node newNode = new Node();
            newNode.key = key;
            cache.put((K) key, newNode);
            addNode(newNode);
        }
        else {
            moveNodeToHead(node);
        }
    }

    private void moveNodeToHead (Node node) {
        removeNode(node);
        addNode(node);
    }

    private void removeNode (Node node) {
        Node prev = node.prev;
        Node next = node.next;

        prev.next = next;
        next.prev = prev;
    }

    private void addNode (Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    @Override
    public void delete (Object key) {
        Node node = cache.getOrDefault((K) key, null);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public Object getTail () {
        Node node = tail.prev;
        removeNode(node);
        return node.key;
    }
}