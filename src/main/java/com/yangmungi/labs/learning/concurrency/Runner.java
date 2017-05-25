package com.yangmungi.labs.learning.concurrency;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangmungi on 5/24/17.
 */
public class Runner {
    public static void main(String[] args) throws InterruptedException {
        final ListOperator listOperator = new ListOperator();
        final ExecutorService executorService = Executors.newFixedThreadPool(8);

        final PrintStream out = System.out;

        final MultiplyOperator multiplyOperator = new MultiplyOperator();

        final int limit = 10000;
        for (int i = 0; i < limit; i++) {
            final int finalI = i;
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    // operate directly to container
//                    final List<Integer> integers = listOperator.getContainer();
//                    final SingleOperatorList integerList = new SingleOperatorList(integers);
//                    operateAndPrint(integerList, finalI, "direct");
//
//                }
//            });
//
//            final int secondI = i + limit;
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    operateAndPrint(listOperator, secondI, "synchronized");
//                }
//            });

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    operateAndPrint(multiplyOperator, finalI, "mult-unsafe");
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(100L, TimeUnit.SECONDS);

        out.println(listOperator.getStatistic());

    }

    private static <T, R, S> void operateAndPrint(SingleOperator<T, R, S> integerList, R finalI, String descriptor) {
        final Thread thread = Thread.currentThread();

        S stat;
        stat = integerList.getStatistic();
        final String prefix = thread + ":" + finalI + " " + descriptor;
        System.out.println(prefix + "-pre:" + stat);

        integerList.operate(finalI);

        stat = integerList.getStatistic();
        System.out.println(prefix + "-operate:" + stat);

        final R removed = integerList.undo();

        stat = integerList.getStatistic();
        System.out.println(prefix + "-remove:" + stat + " r:" + removed);

        integerList.operate(finalI);
    }
}
