package com.multithreading.problems;

public class BoundedBuffer {
    public static void main(String[] args) throws InterruptedException {
        final BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(5);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
                        blockingQueue.enqueue(i);
                        System.out.println("Enqueued " + i);
                    }
                } catch (InterruptedException ie) {
                    // Handle exception
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 2 dequeued " + blockingQueue.dequeue());
                    }
                } catch (InterruptedException ie) {
                    // Handle exception
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 3 dequeued " + blockingQueue.dequeue());
                    }
                } catch (InterruptedException ie) {
                    // Handle exception
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

class BlockingQueue<T> {

    T[] array;
    int capacity; // max capacity for the queue
    int size = 0; // track realtime size of the queue
    int head = 0; // track from where dequeue will happen
    int tail = 0; // track where enqueue will happen
    final Object lock = new Object();

    @SuppressWarnings("unchecked")
    BlockingQueue (int capacity) {
        this.capacity = capacity;
        this.array = (T[]) new Object[capacity];
    }

    public void enqueue(T item) throws InterruptedException {
        synchronized (lock) {
            while (capacity == size) {
                lock.wait();
            }

            if (tail == capacity) {
                tail = 0;
            }

            array[tail] = item;
            tail ++;
            size ++;
            lock.notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {

        T item = null;

        synchronized (lock) {
            while (size == 0) {
                lock.wait();
            }

            if (head == capacity) {
                head = 0;
            }

            item = array[head];
            array[head] = null;
            head ++;
            size --;
            lock.notifyAll();
        }

        return item;
    }
}