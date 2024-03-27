package com.multithreading.snippets;

import java.util.concurrent.Executor;

public class ThreadExecutor {
    public static void main(String[] args) {
        DumbExecutor dumbExecutor = new DumbExecutor();
        dumbExecutor.execute(new MyTask());
    }
}

class DumbExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        Thread newThread = new Thread(command);
        newThread.start();
    }
}

class MyTask implements Runnable {
    public void run() {
        System.out.println("My Task is running now");
    }
}
