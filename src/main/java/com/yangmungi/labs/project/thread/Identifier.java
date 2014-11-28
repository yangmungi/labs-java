package com.yangmungi.labs.project.thread;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by Yangmun on 7/6/2014.
 */
public class Identifier {
    private final static String GLOBAL_NAMESPACE = "__global";
    private final ConcurrentMap<String, Integer> sequenceMap;
    private int failures = 0;
    private int successes = 0;

    public Identifier(ConcurrentMap<String, Integer> sequenceMap) {
        this.sequenceMap = sequenceMap;
    }

    public ConcurrentMap<String, Integer> getSequenceMap() {
        return sequenceMap;
    }

    public int getNext() {
        return getNextSequence(GLOBAL_NAMESPACE);
    }

    public int getNextSequence(String namespace) {
        Integer old = sequenceMap.putIfAbsent(namespace, 1);

        boolean failed = old != null;
        if (!failed) {
            old = 1;
        }

        while (failed) {
            old = sequenceMap.get(namespace);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            failed = !sequenceMap.replace(namespace, old, old + 1);
            if (failed) {

                // Summing Queue
                failures++;
            }
            System.out.println((failed ? "FAIL " : "OK ") + failures + " " + Thread.currentThread()
                    + " " + namespace + " : " + old);
        }

        successes++;

        return old;
    }

    private void increment(String namespace) {
        Integer old = sequenceMap.get(namespace);
        sequenceMap.replace(namespace, old, old + 1);
    }

    public double getSuccessRatio() {
        if (failures > 0 && successes > 0) {
            return (double)successes / (failures + successes);
        } else {
            return 1.0;
        }
    }
}