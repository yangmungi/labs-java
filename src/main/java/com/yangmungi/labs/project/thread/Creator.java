package com.yangmungi.labs.project.thread;

import java.util.Random;

/**
* Created by Yangmun on 7/6/2014.
*/
public class Creator implements Runnable {
    private final Random random;
    private final Identifier identifier;

    private int modMax;

    public Creator(Identifier identifier, Random random) {
        this.identifier = identifier;
        this.random = random;
    }

    public int getModMax() {
        return modMax;
    }

    public void setModMax(int modMax) {
        this.modMax = modMax;
    }

    @Override
    public void run() {
        int nextInt = Math.abs(random.nextInt());
        int modMax = getModMax();

        String namespace = String.valueOf(nextInt % modMax);
        for (int i = 0; i < 15; i++) {
            namespace = String.valueOf(++nextInt % modMax);
            int next = identifier.getNextSequence(
                    namespace
            );
        }
    }
}
