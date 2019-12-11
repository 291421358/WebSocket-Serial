package com.laola.hello.laola.utils;

public class ThreadStop extends Thread {
    //保证尽快开始处理
    private volatile boolean shutdownRequested = false;

    public final void run() {
        while (!shutdownRequested) {
            doWork();
        }
        try {
            doShutDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void doWork() {
        System.out.println("doWork");
    }

    void doShutDown() throws InterruptedException {
        shutdownRequested = true;
        interrupt();
    }

    public static void main(String[] args) throws Exception {
        ThreadStop ts = new ThreadStop();
        ts.start();
        ts.doShutDown();
    }
}
