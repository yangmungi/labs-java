package com.yangmungi.labs.learning.concurrency;

import java.util.List;

/**
 * Created by yangmungi on 5/24/17.
 */
class SingleOperatorList implements SingleOperator<List<Integer>, Integer, Integer> {
    private final List<Integer> integers;

    public SingleOperatorList(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    public List<Integer> getContainer() {
        return integers;
    }

    @Override
    public void operate(Integer integer) {
        integers.add(integer);
    }

    @Override
    public Integer undo() {
        return integers.remove(0);
    }

    @Override
    public Integer getStatistic() {
        return integers.size();
    }
}
