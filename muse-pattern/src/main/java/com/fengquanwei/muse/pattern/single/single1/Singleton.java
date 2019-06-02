package com.fengquanwei.muse.pattern.single.single1;

/**
 * 懒汉式（线程不安全）
 *
 * @author fengquanwei
 * @create 2019/6/2 下午2:36
 **/
public class Singleton {
    private static Singleton INSTANCE;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            try {
                // 模拟准备工作（测试多线程不安全性）
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            INSTANCE = new Singleton();
        }

        return INSTANCE;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        int threadSize = 10;

        Thread[] threads = new Thread[threadSize];

        for (int i = 0; i < threadSize; i++) {
            threads[i] = new Thread(() -> System.out.println(Singleton.getInstance()));
        }

        for (int i = 0; i < threadSize; i++) {
            threads[i].start();
        }
    }
}
