package com.yangmungi.labs.learning.concurrency;

/**
 * Created by yangmungi on 5/24/17.
 */
interface SingleOperator<Container, Operand, Statistic> {
    Container getContainer();
    void operate(Operand operand);
    Operand undo();
    Statistic getStatistic();
}
