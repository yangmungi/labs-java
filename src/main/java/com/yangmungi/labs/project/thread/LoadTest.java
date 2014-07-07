package com.yangmungi.labs.project.thread;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yangmun on 7/5/2014.
 */
public class LoadTest {

    public static void main(String[] args) {
        Random random = new Random();

        System.out.println("Start");

        Set<Thread> globalThreads = new HashSet<Thread>();

        //
        Identifier identifier = new Identifier();
        Thread identifierThread = new Thread();

        globalThreads.add(identifierThread);

        identifierThread.start();

        //
        Set<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 5000; i++) {
            Creator creator = new Creator(identifier, random);
            Thread thread = new Thread(creator);
            threads.add(thread);

            globalThreads.add(thread);
        }

        System.out.println("Spawned");

        for (Thread thread : threads) {
            thread.start();
        }

        System.out.println("Started");

        try {
            for (Thread thread : globalThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(identifier.getSequenceMap());
        System.out.println("Done");
    }
}
