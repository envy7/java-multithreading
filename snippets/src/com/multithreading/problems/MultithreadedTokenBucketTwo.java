package com.multithreading.problems;

import java.util.HashSet;
import java.util.Set;

public class MultithreadedTokenBucketTwo {

    public static void main(String[] args) throws InterruptedException {
        final IMTTokenBucketFilter mtTokenBucketFilter = MTTokenBucketFilterFactory.makeMTTokenBucketFilter(1);

        Set<Thread> allThreads = new HashSet<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mtTokenBucketFilter.getToken();
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

interface IMTTokenBucketFilter {
    void getToken() throws InterruptedException;
}

final class MTTokenBucketFilterFactory {

    // Force users to interact with the factory only through static methods
    private MTTokenBucketFilterFactory() {}

    static public IMTTokenBucketFilter makeMTTokenBucketFilter(long capacity) {
        MTTokenBucketFilter tbf = new MTTokenBucketFilter(capacity);
        tbf.initialize();
        return tbf;
    }

    private static class MTTokenBucketFilter implements IMTTokenBucketFilter {
        private long currentTokens = 0;
        private final long MAX_TOKENS;
        private final int TOKEN_FILL_RATE = 1000; // in milliseconds

        MTTokenBucketFilter(long maxTokens) {
            this.MAX_TOKENS = maxTokens;
        }

        void initialize() {
            Thread dt = new Thread(this::daemonThread);
            dt.setDaemon(true);
            dt.start();
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

        public synchronized void getToken() throws InterruptedException {

            while (currentTokens == 0) {
                this.wait();
            }
            currentTokens --;

            System.out.println("Granting " + Thread.currentThread().getName() + " token at " +
                    (System.currentTimeMillis() / 1000));
        }
    }

}


/*
1. syncs infrastructure builds framework cdm
2. 7 engineers
3. 90% GO, mysql, kubernetes
4. really good at coding, distributed sysyem
 */


// algo coding - leetcode med/hard - talk - code and talk through - clean bug free session - take hints - problem solving - critical thinking - 1hr
// 1 algo
// 2 systems coding - ds/os multithreading locks thread s in o file sysytem calls semaphores talk through the code
// 45mins with hiring manager, thoughtful questions team impact