public class Node<K, V> {
    private final K key;
    private final V value;
    private Node<K, V> prev;
    private Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        next = prev = null;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public Node<K, V> getPrev() {
        return prev;
    }

    public void setPrev(Node<K, V> prev) {
        this.prev = prev;
    }
}
