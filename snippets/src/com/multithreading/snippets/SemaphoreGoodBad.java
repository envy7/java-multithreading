package com.multithreading.snippets;

import java.util.concurrent.Semaphore;

public class SemaphoreGoodBad {
    public static void main(String[] args) throws InterruptedException {
        // BadSemaphoreExample.example();
        GoodSemaphoreExample.example();
    }
}

class BadSemaphoreExample {
    public static void example() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);

        final Thread badThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException ie) {
                        // handle exception
                    }

                    // Simulate a bad thread that is meant to run forever but crashes
                    throw new RuntimeException("Exception happens at runtime");
                }
            }
        });

        badThread.start();

        Thread.sleep(1000);

        final Thread goodThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Good thread patiently waiting to be signalled");
                try {
                    semaphore.acquire();
                } catch (InterruptedException ie) {
                    // handle thread interruption
                }
            }
        });

        goodThread.start();
        badThread.join();
        goodThread.join();
        System.out.println("Exiting program");

    }
}

class GoodSemaphoreExample {
    public static void example() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);

        final Thread badThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        semaphore.acquire();
                        try {
                            throw new RuntimeException("");
                        } catch (Exception e) {
                            // handle logic
                            return;
                        } finally {
                            System.out.println("Bad thread releasing semaphore");
                            semaphore.release();
                        }
                    } catch (InterruptedException ie) {
                        // handle thread interruption
                    }
                }
            }
        });

        badThread.start();

        Thread.sleep(1000);

        final Thread goodThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Good thread patiently waiting to be signalled");
                try {
                    semaphore.acquire();
                } catch (InterruptedException ie) {
                    // handle thread interruption
                }
            }
        });

        goodThread.start();
        badThread.join();
        goodThread.join();
        System.out.println("Exiting program");

    }
}