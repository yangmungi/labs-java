package com.yangmungi.labs.project.thread;

import java.util.Map;
import java.util.Random;

/**
* Created by Yangmun on 7/6/2014.
*/
public class Creator implements Runnable {
    private final Random random;
    private final Map<Integer, Double> map;

    public Creator(Random random, Map<Integer, Double> map) {
        this.random = random;
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            map.put(random.nextInt(), random.nextGaussian());
        }
    }
}
