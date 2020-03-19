package main.java.ru.ifmo.rain.khusainov.lrucache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final Map<K, Node<K, V>> cache;
    private Node<K, V> head;
    private Node<K, V> tail;
    private final int capacity;

    public LRUCache(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
        head = null;
        cache = new HashMap<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        Node<K, V> node = cache.get(key);
        remove(node);
        rebase(node);
        return node.getValue();
    }

    public void put(K key, V value) {
        assert cache.size() <= capacity;
        Node<K, V> node = new Node<>(null, null, key, value);

        if (cache.containsKey(key)) {
            Node<K, V> oldNode = cache.get(key);
            remove(oldNode);
        } else if (cache.size() == capacity) {
            assert tail != null;
            K most_recent_used_key = tail.getKey();
            assert cache.containsKey(most_recent_used_key);
            cache.remove(most_recent_used_key);

            if (tail.prev == null) {
                assert capacity == 1 && tail == head;
                tail = node;
            } else {
                tail = tail.prev;
                tail.next = null;
            }
        }
        cache.put(key, node);
        if (cache.size() == 1) {
            tail = node;
            head = node;
        } else {
            rebase(node);
        }
    }

    public int size() {
        return cache.size();
    }

    private void rebase(Node<K, V> node) {
        assert node != null;
        node.prev = null;
        node.next = head;
        if (head != node && head != null) {
            head.prev = node;
        }
        head = node;
    }

    private void remove(Node<K, V> node) {
        assert node != null;
        Node<K, V> prev = node.prev;
        if (prev != null) {
            assert head != node;
            prev.next = node.next;
            if (node.next == null) {
                assert node == tail;
                tail = prev;
            } else {
                node.next.prev = prev;
            }
        } else {
            assert head == node;
            head = head.next;
            if(head == null){
                tail = null;
            } else {
                head.prev = null;
            }
        }
    }
}
