package Practice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class CacheManagement {
    public static void main(String[] args) {
        Cache<Integer, Integer> lru = new LRUCache<>(3);
        lru.put(1, 1);
        lru.put(2, 2);
        System.out.println(lru.get(2));
        lru.put(3, 3);
        lru.put(4, 4);
        System.out.println(lru.get(1));

        System.out.println("----------------------");
        Cache<Integer, Integer> lfu = new LFUCache<>(3);
        lfu.put(1, 1);
        lfu.put(2, 2);
        lfu.put(3, 3);
        System.out.println(lfu.get(2));
        System.out.println(lfu.get(2));
        System.out.println(lfu.get(3));
        System.out.println(lfu.get(3));
        System.out.println(lfu.get(1));
        lfu.put(4, 4);
        System.out.println(lfu.get(1));
        System.out.println(lfu.get(2));
    }
}

interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
}

abstract class BaseCache<K, V>  implements Cache<K, V>{
    int capacity;
    HashMap<K, V> cacheMap;
    EvictionPolicy<K> evictionPolicy;
    public BaseCache(int _capacity, EvictionPolicy<K> _evictionPolicy) {
        capacity = _capacity;
        cacheMap = new HashMap<>();
        evictionPolicy = _evictionPolicy;
    }
    @Override
    public synchronized V get(K key) {
        if(!cacheMap.containsKey(key)) return null;
        evictionPolicy.keyAccessed(key);
        return cacheMap.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        if(cacheMap.size() >= capacity) {
            K evictedKey = evictionPolicy.evictKey();
            cacheMap.remove(evictedKey);
        }
        evictionPolicy.keyAccessed(key);
        cacheMap.put(key, value);
    }
}

class LRUCache<K, V> extends BaseCache<K, V> {
    public LRUCache(int _capacity) {
        super(_capacity, new LRUEvictionPolicy<>());
    }
}

class LFUCache<K, V> extends BaseCache<K, V> {
    public LFUCache(int _capacity) {
        super(_capacity, new LFUEvictionPolicy<>());
    }
}

interface EvictionPolicy<K> {
    void keyAccessed(K k);
    K evictKey();
}

class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    DoublyLinkedList<K> doublyLL = new DoublyLinkedList<>();
    HashMap<K, Node<K>> map = new HashMap<>();
    @Override
    public void keyAccessed(K k) {
        if(map.containsKey(k)) doublyLL.removeNode(map.get(k));
        Node<K> node = new Node<>(k);
        doublyLL.addFirst(node);
        map.put(k, node);
    }

    @Override
    public K evictKey() {
        Node<K> node = doublyLL.removeLast();
        map.remove(node.data);
        return node.data;
    }
}

class LFUEvictionPolicy<K> implements EvictionPolicy<K> {
    HashMap<K, Integer> keyFreqMap = new HashMap<>();
    HashMap<Integer, LinkedHashSet<K>> freqBucket = new HashMap<>();
    int minFreq = 0;
    @Override
    public void keyAccessed(K key) {
        int currFreq = keyFreqMap.getOrDefault(key, 0), newFreq = currFreq+1;
        if(currFreq > 0) {
            LinkedHashSet<K> currFreqSet = freqBucket.getOrDefault(currFreq, null);
            if(currFreqSet != null) {
                currFreqSet.remove(key);
                if(currFreqSet.isEmpty()) {
                    freqBucket.remove(currFreq);
                    if(minFreq == currFreq) minFreq++;
                }
            }
        }
        keyFreqMap.put(key, newFreq);
        freqBucket.computeIfAbsent(newFreq, freq -> new LinkedHashSet<K>()).add(key);
        if(currFreq == 0) minFreq = 1;
    }

    @Override
    public K evictKey() {
        LinkedHashSet<K> minFreqSet = freqBucket.getOrDefault(minFreq, null);
        if(minFreqSet == null || minFreqSet.isEmpty()) return null;
        Iterator<K> it = minFreqSet.iterator();
        K key = it.next();
        it.remove();
        keyFreqMap.remove(key);
        if(minFreqSet.isEmpty()) {
            freqBucket.remove(minFreq);
            minFreq++;
        }
        return key;
    }
}

class DoublyLinkedList<T> {
    Node<T> head, tail;

    public Node<T> removeLast() {
        if(tail == null) return null;
        Node<T> removed = tail;
        tail = tail.prev;
        return removed;
    }

    public void addFirst(Node<T> node) {
        if(head == null) head = tail = node;
        head.prev = node;
        node.nxt = head;
        head = node;
    }

    public void removeNode(Node<T> node) {
        if(node == null) return;
        if(head == node) head = head.nxt;
        if(tail == node) tail = tail.prev;
        if(node.nxt != null) node.nxt.prev=  node.prev;
        if(node.prev != null) node.prev.nxt = node.nxt;
    }

}

class Node<T> {
    T data;
    Node<T> prev, nxt;

    Node(T _value) {
        data = _value;
        prev = nxt = null;
    }
}
