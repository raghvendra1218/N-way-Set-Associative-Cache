package com.raghvendra;

import java.util.HashMap;
import java.util.Map;

public class MruCache<K> extends Node<K> implements CacheEvictionPolicy<K> {
    private Map<K, Node> cache;
    private Node head;
    private Node tail;

    public MruCache () {
        cache = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }


    @Override
    public void put(K key) {
        Node node = cache.getOrDefault((K) key, null);

        if (node == null) {
            Node newNode = new Node();
            newNode.key = key;
            cache.put((K) key, newNode);
            addNode(newNode);
        }
        else {
            moveNodeToTail(node);
        }

    }

    private void moveNodeToTail(Node node){
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
        node.prev = tail;
        tail.next = node;
        tail = node;
    }

    @Override
    public void delete(Object key) {
        Node node = cache.getOrDefault((K) key, null);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public Node getTail() {
        return tail;
    }
}


