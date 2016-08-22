package com.bzh;

public class Main {


    static Object lock1 = new Object();
    static Object lock2 = new Object();


    public static void main(String[] args) {
        Bussiness bussiness = new Bussiness();
        new Thread(new Runnable() {
            @Override
            public void run() {
                bussiness.MainThread();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                bussiness.SubThread();
            }
        }).start();

    }


    static class Bussiness {
        private Object LOCK = new Object();
        volatile boolean bShouldSub = true;//这里相当于定义了控制该谁执行的一个信号灯

        public void MainThread() {
            while (true)
                synchronized (LOCK) {//notify和wait的对象一定要和synchronized的对象保持一致
                    for (int j = 0; j < 5; j++) {
                        System.out.println(Thread.currentThread().getName() + "MainThread j=" + j);
                    }
                    if (bShouldSub) {
                        bShouldSub = false;
                        LOCK.notify();
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generatedcatch block
                            e.printStackTrace();
                        }
                    }
                }
        }


        public void SubThread() {
            while (true)
                synchronized (LOCK) {
                    for (int j = 0; j < 5; j++) {
                        System.out.println(Thread.currentThread().getName() + "+SubThread:j=" + j);
                    }
                    if (!bShouldSub) {
                        bShouldSub = true;
                        LOCK.notify();
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generatedcatch block
                            e.printStackTrace();
                        }
                    }
                }
        }
    }

}
