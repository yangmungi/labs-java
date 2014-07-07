package com.yangmungi.labs.project.thread;

import java.util.concurrent.ConcurrentMap;

/**
* Created by Yangmun on 7/6/2014.
*/
public class Identifier {
    private final static String GLOBAL_NAMESPACE = "__global";
    private ConcurrentMap<String, Integer> sequenceMap;

    public ConcurrentMap<String, Integer> getSequenceMap() {
        return sequenceMap;
    }

    public int getNextSequence(String namespace) {
        int sequence = 1;
        if (!sequenceMap.containsKey(namespace)) {
            sequenceMap.put(namespace, sequence);
        } else {
            sequence = sequenceMap.get(namespace);
            sequence++;
            sequenceMap.put(namespace, sequence);
        }

        return sequence;
    }
}

