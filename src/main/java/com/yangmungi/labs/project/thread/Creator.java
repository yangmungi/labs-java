package com.yangmungi.labs.project.thread;

import java.util.Map;
import java.util.Random;

/**
* Created by Yangmun on 7/6/2014.
*/
public class Creator implements Runnable {
    private final Random random;
    private final Identifier identifier;

    public Creator(Identifier identifier, Random random) {
        this.identifier = identifier;
        this.random = random;
    }

    @Override
    public void run() {
        //identifier.getNext();
    }
}
