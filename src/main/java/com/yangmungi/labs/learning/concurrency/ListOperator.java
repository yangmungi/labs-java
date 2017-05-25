package com.yangmungi.labs.learning.concurrency;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by yangmungi on 5/21/17.
 */
public class ListOperator implements SingleOperator<List<Integer>, Integer, Integer> {
    private final List<Integer> integerList;

    public ListOperator() {
        this.integerList = new CopyOnWriteArrayList<Integer>();
    }

    public List<Integer> getContainer() {
        return integerList;
    }

    public synchronized void operate(Integer i) {
        integerList.add(i);
    }

    public synchronized Integer undo() {
        return integerList.remove(0);
    }

    public synchronized Integer getStatistic() {
        return integerList.size();
    }
}
