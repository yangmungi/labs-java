package com.yangmungi.labs.sim.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

/**
 * Delegates messages to other callables.
 * Created by Yangmun on 7/6/2014.
*/
public class MessageReader<T> implements Callable<T> {
    private final ConcurrentMap<String, T> inputPipe;

    public MessageReader(ConcurrentMap<String, T> inputPipe) {
        this.inputPipe = inputPipe;
    }

    @Override
    public T call() throws Exception {
        return null;
    }
}