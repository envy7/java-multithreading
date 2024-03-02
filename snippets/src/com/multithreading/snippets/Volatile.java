package com.multithreading.snippets;

import java.util.ArrayList;
import java.util.List;

public class Volatile {

    // volatile doesn't imply thread safety
    static volatile int count = 0;

    public static void main(String[] args) throws InterruptedException {

        int numThreads = 10;
        List<Thread> threads = new ArrayList<>(numThreads);

        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        count ++;
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }

        System.out.println("The count is " + count);
    }
}
