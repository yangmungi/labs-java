package com.yangmungi.labs.learning.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangmungi on 5/24/17.
 */
public class MultiplyOperator implements SingleOperator<Integer, Integer, MultiplyOperator.Statistic> {
    private int container = 1;

    private final AtomicInteger operateCounter = new AtomicInteger();
    private final AtomicInteger undoCounter = new AtomicInteger();
    private final AtomicInteger skipCounter = new AtomicInteger();

    @Override
    public Integer getContainer() {
        return container;
    }

    @Override
    public void operate(Integer integer) {
        int result = container * (integer + 1);
        if (result > 0) {
            container = result;
            operateCounter.incrementAndGet();
        } else {
            skipCounter.incrementAndGet();
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
        int undoCount = undoCounter.get();
        int operateCount = operateCounter.get();
        int skipCount = skipCounter.get();
        return new Statistic(container, undoCount, operateCount, skipCount);
    }

    static class Statistic {
        public final int container;
        public final int undoCounter;
        public final int operateCounter;
        public final int skipCount;

        public Statistic(int container, int undoCount, int operateCount, int skipCount) {
            this.container = container;
            this.undoCounter = undoCount;
            this.operateCounter = operateCount;
            this.skipCount = skipCount;
        }

        @Override
        public String toString() {
            return "Statistic{" +
                    "container=" + container +
                    ", undoCounter=" + undoCounter +
                    ", operateCounter=" + operateCounter +
                    ", skipCount=" + skipCount +
                    '}';
        }
    }
}
