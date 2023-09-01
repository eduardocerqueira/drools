package org.drools.verifier.core.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiSet<K, V> {

    private Map<K, HashSet<V>> map = new HashMap<>();

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean put(K key,
                       V value) {
        if (map.containsKey(key)) {
            return map.get(key).add(value);
        } else {
            HashSet<V> list = new HashSet<>();
            list.add(value);
            map.put(key, list);
            return true;
        }
    }

    public void putAllValues(K key,
                             HashSet<V> values) {
        map.put(key, values);
    }

    public boolean addAllValues(K key,
                                Collection<V> values) {
        if (map.containsKey(key)) {
            return map.get(key).addAll(values);
        } else {
            HashSet<V> set = new HashSet<>();
            set.addAll(values);
            map.put(key, set);
            return true;
        }
    }

    public Collection<V> remove(K key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Set<K> keys() {
        return map.keySet();
    }

    public Collection<V> get(K key) {
        return map.get(key);
    }

    public void clear() {
        map.clear();
    }

    public List<V> allValues() {
        ArrayList<V> allValues = new ArrayList<>();

        for (K k : keys()) {
            allValues.addAll(get(k));
        }

        return allValues;
    }

    public void removeValue(K k,
                            V v) {
        get(k).remove(v);
    }
}
