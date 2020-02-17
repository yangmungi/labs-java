package com.yangmungi.labs.sim.thread;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yangmun on 7/5/2014.
 */
public class LoadTest {

    public static void main(String[] args) {
        call();
    }

    public static void call() {

    }

    public static void thread() {
        Random random = new Random();

        Set<Thread> globalThreads = new HashSet<Thread>();

        // @todo load increaser
        Identifier identifier = new Identifier(new ConcurrentHashMap<String, Integer>());

        // @todo thread manager
        Set<Thread> threads = new HashSet<Thread>();
        int threadCount = 0;

        int finishedCount = 0;
        int threadCap = 200;
        int neededCount = 15000;

        while (finishedCount < neededCount) {
            if (threadCount < threadCap) {
                Creator creator = new Creator(identifier, random);
                creator.setModMax(100);

                Thread thread = new Thread(creator);
                threads.add(thread);

                globalThreads.add(thread);

                threadCount++;
            }

            for (Thread thread : threads) {
                final Thread.State state = thread.getState();
                if (state.equals(Thread.State.NEW)) {
                    thread.start();
                } else if (state.equals(Thread.State.TERMINATED)) {
                    finishedCount++;
                    threadCount--;
                    globalThreads.remove(thread);
                }
            }

            try {
                for (Thread thread : globalThreads) {
                    thread.join(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            for (Thread thread : globalThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(identifier.getSuccessRatio());
        System.out.println("Done");
    }
}
