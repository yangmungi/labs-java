package com.yangmungi.labs.sim.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yangmun on 4/16/2016.
 */
public class Counter {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        final AtomicInteger shared = new AtomicInteger(1);

        final LinkedBlockingDeque<Integer> buffer = new LinkedBlockingDeque<Integer>();

        final AtomicBoolean running = new AtomicBoolean(true);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    while (running.get()) {
                        int received;
                        try {
                            System.out.println("poll()");
                            received = buffer.take();
                            System.out.println(received);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("reader stopping");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            final int index = i;

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        int atomic = shared.getAndIncrement();
                        System.out.println("R:" + index + " offer:" + atomic);
                        buffer.offer(atomic);
                        synchronized (buffer) {
                            buffer.notify();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (!buffer.isEmpty()) {
                    try {
                        System.out.println("Sleep");
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                running.set(false);
            }
        });

        executorService.shutdown();
    }
}
