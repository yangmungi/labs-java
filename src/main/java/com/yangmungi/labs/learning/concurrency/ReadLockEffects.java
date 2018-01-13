package com.yangmungi.labs.learning.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yangmungi on 5/25/17.
 */
public class ReadLockEffects {
    public static void main(String[] args) throws InterruptedException {
        // read into thread List
        // lock and write
        // read from thread without lock
        final List<Integer> integers = new ArrayList<Integer>();
        final AtomicBoolean keepPrinting = new AtomicBoolean(true);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (keepPrinting.get()) {
                        System.out.println("P:" + integers.size());
                        Thread.sleep(1L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        final Lock lock = new ReentrantLock();

        final Magic magic = new Magic();

        final Random random = new Random();
        final ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            final int myI = i;
            final boolean shouldDoMagic = random.nextBoolean();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    final long id = Thread.currentThread().getId();
                    System.out.println(id + " free:" + integers.size() + " M:" + magic.getMagic());
                    if (shouldDoMagic) {
                        System.out.println(id + " do magic");
                        magic.magic();
                    }

                    lock.lock();
                    System.out.println(id + " lock:" + integers.size() + " M:" + magic.getMagic());

                    try {
                        integers.add(myI);
                        System.out.println(id + " adde:" + integers.size() + " M:" + magic.getMagic());
                    } finally {
                        lock.unlock();
                    }

                    System.out.println(id + " exit:" + integers.size() + " M:" + magic.getMagic());
                }
            });
        }

        keepPrinting.set(false);
        executorService.shutdown();
        thread.join();
    }

    static class Magic {
        private int magic = 0;

        public int getMagic() {
            return magic;
        }

        void magic() {
            magic += 1;
        }
    }
}
