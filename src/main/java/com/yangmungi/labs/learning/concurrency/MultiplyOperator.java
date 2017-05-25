package com.yangmungi.labs.learning.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangmungi on 5/24/17.
 */
public class MultiplyOperator implements SingleOperator<Integer, Integer, MultiplyOperator.Statistic> {
    private int container = 1;

    private final AtomicInteger operateCounter = new AtomicInteger();
    private final AtomicInteger undoCounter = new AtomicInteger();

    @Override
    public Integer getContainer() {
        return container;
    }

    @Override
    public void operate(Integer integer) {
        if (container < Integer.MAX_VALUE) {
            container *= (integer + 1);
            operateCounter.incrementAndGet();
        }
    }

    @Override
    public Integer undo() {
        int preContainer = container;
        if (preContainer > 1) {
            container /= 2;
            undoCounter.incrementAndGet();
        }

        return preContainer;
    }

    @Override
    public Statistic getStatistic() {
        return new Statistic(container, undoCounter, operateCounter);
    }

    static class Statistic {
        public final int container;
        public final int undoCounter;
        public final int operateCounter;

        public Statistic(int container, AtomicInteger undoCounter, AtomicInteger operateCounter) {
            this.container = container;
            this.undoCounter = undoCounter.get();
            this.operateCounter = operateCounter.get();
        }

        @Override
        public String toString() {
            return "Statistic{" +
                    "container=" + container +
                    ", undoCounter=" + undoCounter +
                    ", operateCounter=" + operateCounter +
                    '}';
        }
    }
}
