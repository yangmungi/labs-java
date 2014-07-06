package com.yangmungi.labs.project.thread;

import java.util.NavigableSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by Yangmun on 7/6/2014.
 */
public class Bus {
    // maybe extend instead of compose, or extend then compose
    private final ConcurrentMap<Object, Expiry> messageBus;

    public Bus(ConcurrentMap<Object, Expiry> messageBus) {
        this.messageBus = messageBus;
    }

    public Set<Object> keySet() {
        return messageBus.keySet();
    }

    public Object putIfAbsent(Object key, Object value) {
        return messageBus.putIfAbsent(key, new Expiry(value));
    }

    public boolean remove(Object key, Object value) {
        return messageBus.remove(key, value);
    }

    public Object replace(Object key, Object value) {
        return messageBus.replace(key, new Expiry(value));
    }

    public Object get(Object key) {
        return messageBus.get(key).getTarget();
    }

    public Object put(Object key, Object value) {
        return messageBus.put(key, new Expiry(value));
    }

    public boolean containsKey(Object key) {
        return messageBus.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return messageBus.containsValue(new Expiry(value));
    }
}
