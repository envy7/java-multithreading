package com.multithreading.problems;

public class BoundedBufferSemaphore {
    public static void main(String[] args) throws InterruptedException {
        final BlockingQueueSemaphore<Integer> blockingQueueSemaphore = new BlockingQueueSemaphore<>(5);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        blockingQueueSemaphore.enqueue(i);
                        System.out.println("Enqueued " + i);
                    }
                } catch (InterruptedException ie) {
                    // handle exception
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Thread 2 dequeued " + blockingQueueSemaphore.dequeue());
                    }
                } catch (InterruptedException ie) {
                    // handle exception
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Thread 3 dequeued " + blockingQueueSemaphore.dequeue());
                    }
                } catch (InterruptedException ie) {
                    // handle exception
                }
            }
        });

        t1.start();
        Thread.sleep(4000);
        t2.start();
        t2.join();

        t3.start();
        t1.join();
        t3.join();
    }
}

class BlockingQueueSemaphore<T> {
    T[] array = null;
    int size = 0;
    int capacity;
    int head = 0;
    int tail = 0;
    CountingSemaphore semLock = new CountingSemaphore(1, 1);
    CountingSemaphore semProducer;
    CountingSemaphore semConsumer;

    @SuppressWarnings("unchecked")
    public BlockingQueueSemaphore(int capacity) {
        this.capacity = capacity;
        this.array = (T[]) new Object[capacity];
        this.semConsumer = new CountingSemaphore(capacity, capacity);
        this.semProducer = new CountingSemaphore(capacity, 0);
    }

    public void enqueue(T item) throws InterruptedException {
        semProducer.acquire();
        semLock.acquire();

        if (tail == capacity) {
            tail = 0;
        }

        array[tail] = item;
        tail ++;
        size ++;

        semLock.release();
        semConsumer.release();
    }

    public T dequeue() throws InterruptedException {

        T item = null;

        semConsumer.acquire();
        semLock.acquire();

        if (head == capacity) {
            head = 0;
        }

        item = array[head];
        array[head] = null;
        head ++;
        size --;

        semLock.release();
        semProducer.release();

        return item;
    }
}

class CountingSemaphore {
    int usedPermits = 0;
    int maxCount;

    public CountingSemaphore(int count) {
        this.maxCount = count;
    }

    public CountingSemaphore(int count, int initialPermits) {
        this.maxCount = count;
        this.usedPermits = this.maxCount - initialPermits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (usedPermits == maxCount) {
            wait();
        }

        notify();
        usedPermits ++;
    }

    public synchronized void release() throws InterruptedException {
        while (usedPermits == 0) {
            wait();
        }

        usedPermits --;
        notify();
    }
}
