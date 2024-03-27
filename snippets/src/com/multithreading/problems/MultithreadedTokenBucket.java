package com.multithreading.problems;

import java.util.HashSet;
import java.util.Set;

public class MultithreadedTokenBucket {

    public static void main(String[] args) throws InterruptedException {
        final MultithreadedTokenBucketFilter multithreadedTokenBucketFilter =
                new MultithreadedTokenBucketFilter(1);

        Set<Thread> allThreads = new HashSet<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        multithreadedTokenBucketFilter.getToken();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Thread_" + (i+1));
            allThreads.add(thread);
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }
}

class MultithreadedTokenBucketFilter {
    private long currentTokens = 0;
    private final long MAX_TOKENS;
    private final int TOKEN_FILL_RATE = 1000; // in milliseconds

    MultithreadedTokenBucketFilter(long maxTokens) {
        this.MAX_TOKENS = maxTokens;

        // Starting thread in a constructor is a bad idea
        Thread dt = new Thread(this::daemonThread);
        dt.setDaemon(true);
        dt.start();
    }

    synchronized void getToken() throws InterruptedException {

        while (currentTokens == 0) {
            this.wait();
        }
        currentTokens --;

        System.out.println("Granting " + Thread.currentThread().getName() + " token at " +
                (System.currentTimeMillis() / 1000));
    }

    void daemonThread() {
        while (true) {
            synchronized (this) {
                if (currentTokens < MAX_TOKENS) {
                    currentTokens ++;
                }
                this.notify();
            }

            try {
                Thread.sleep(TOKEN_FILL_RATE);
            } catch (InterruptedException ie) {
                // catch exception
            }
        }
    }

}