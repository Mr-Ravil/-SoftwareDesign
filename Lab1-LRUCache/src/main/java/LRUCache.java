import java.util.HashMap;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final HashMap<K, Node<K, V>> keyToNode;
    private Node<K, V> head;
    private Node<K, V> tail;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }

        this.capacity = capacity;
        this.keyToNode = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    public V get(K key) {
        assert key != null;

        if (!keyToNode.containsKey(key)) return null;

        Node<K, V> node = keyToNode.get(key);
        recallNode(node);

        assert node.getKey().equals(key);
        assert head == node;

        return node.getValue();
    }

    public void put(K key, V value) {
        assert key != null;

        if (keyToNode.containsKey(key)) {
            eraseNode(keyToNode.get(key));
        }

        Node<K, V> newNode = new Node<>(key, value);

        keyToNode.put(key, newNode);
        addFirst(newNode);

        if (keyToNode.size() > capacity) {
            forgetNode();
        }

        assert head == newNode;
        assert keyToNode.containsKey(key);
        assert keyToNode.get(key) == newNode;
        assert keyToNode.size() <= capacity;
    }


    private void recallNode(Node<K, V> node) {
        assert node != null;

        if (node == head) return;

        eraseNode(node);
        addFirst(node);

        assert node == head;
    }

    private void forgetNode() {
        assert head != null;
        assert tail != null;
        assert keyToNode.size() > capacity;

        Node<K, V> oldNode = tail;

        keyToNode.remove(oldNode.getKey());
        eraseNode(oldNode);

        assert oldNode != tail;
        assert !keyToNode.containsKey(oldNode.getKey());
    }

    private void addFirst(Node<K, V> node) {
        assert node != null;

        if (head == null) {
            head = tail = node;
        } else {
            node.setNext(head);
            head.setPrev(node);
            head = node;
        }

        assert head != null;
        assert tail != null;
    }

    private void eraseNode(Node<K, V> node) {
        assert node != null;

        Node<K, V> prev = node.getPrev();
        Node<K, V> next = node.getNext();

        if (node == head) {
            head = node.getNext();
            head.setPrev(null);
        } else {
            prev.setNext(node.getNext());
        }

        if (node == tail) {
            tail = node.getPrev();
            tail.setNext(null);
        } else {
            next.setPrev(node.getPrev());
        }

        node.setPrev(null);
        node.setNext(null);

        assert prev == null || prev.getNext() == next;
        assert next == null || next.getPrev() == prev;
        assert node != head;
        assert node != tail;
    }

}
