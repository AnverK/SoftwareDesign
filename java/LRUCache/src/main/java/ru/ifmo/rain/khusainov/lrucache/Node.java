package main.java.ru.ifmo.rain.khusainov.lrucache;

public class Node<K, V> {
    private final K key;
    private final V value;
    public Node<K, V> prev;
    public Node<K, V> next;

    Node(Node<K, V> prev, Node<K, V> next, K key, V value) {
        this.key = key;
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
