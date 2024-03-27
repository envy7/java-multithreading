package com.multithreading.problems;


import java.util.HashSet;
import java.util.Set;

public class TokenBucket {
    public static void main(String[] args) throws InterruptedException {
        final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(1);

        Set<Thread> allThreads = new HashSet<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tokenBucketFilter.getToken();
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

class TokenBucketFilter {

    private final long MAX_TOKENS;

    // in seconds
    private final int TOKEN_FILL_RATE = 1;
    private long lastRequestTime = System.currentTimeMillis();
    private long currentTokens = 0;

    // Will need a constructor to initialize the max size
    TokenBucketFilter(int maxTokens) {
        this.MAX_TOKENS = maxTokens;
    }

    /*
    1. Get current request time
    2. Compare with last request time to check how many tokens added depending on the rate
    3. If current tokens = 0 then wait
     */
    synchronized void getToken() throws InterruptedException {

        currentTokens += (System.currentTimeMillis() - lastRequestTime) / (1000*TOKEN_FILL_RATE);

        if (currentTokens > MAX_TOKENS) {
            currentTokens = MAX_TOKENS;
        }

        if (currentTokens == 0) {
            Thread.sleep(1000);
        } else {
            currentTokens --;
        }

        lastRequestTime = System.currentTimeMillis();
        System.out.println(lastRequestTime);

        System.out.println("Granting " + Thread.currentThread().getName() + " token at " +
                (System.currentTimeMillis() / 1000));
    }
}
