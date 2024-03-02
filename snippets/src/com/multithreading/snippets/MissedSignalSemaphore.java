package com.multithreading.snippets;

import java.util.concurrent.Semaphore;

public class MissedSignalSemaphore {
    public static void main(String[] args) throws InterruptedException {
        FixedMissedSignal.example();
    }
}

class FixedMissedSignal {
    public static void example() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);

        Thread signaller = new Thread(new Runnable() {
            @Override
            public void run() {
                semaphore.release();
                System.out.println("Sent Signal");
            }
        });

        Thread waiter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println("Received signal");
                } catch (InterruptedException ie) {
                    // handle interruption
                }
            }
        });

        signaller.start();
        signaller.join();
        Thread.sleep(5000);
        waiter.start();
        waiter.join();

        System.out.println("Program exiting");
    }
}
