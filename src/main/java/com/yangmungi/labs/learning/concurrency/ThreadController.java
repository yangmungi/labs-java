package com.yangmungi.labs.learning.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yangmungi on 6/2/17.
 */
public class ThreadController {
    public static void main(String[] args) throws InterruptedException {
        final int lockCount = 2;
        final Lock[] locks = new Lock[lockCount];
        for (int i = 0; i < lockCount; i++) {
            locks[i] = new ReentrantLock();
        }

        final List<Runnable> jobs = new ArrayList<Runnable>();

        final int jobsPerLock = 3;

        for (int i = 0; i < locks.length; i++) {
            final Lock lock = locks[i];
            for (int j = 0; j < jobsPerLock; j++) {
                final Runnable attainAndSleep = new Runnable() {
                    @Override
                    public void run() {
                        lock.lock();
                        try {
                            for (int i = 0; i < 10; i++) {
                                Thread.sleep(100L);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock.unlock();
                    }
                };

                jobs.add(attainAndSleep);
            }
        }

        boolean shouldEnd = false;
        final Thread thread = new Thread();
    }
}
