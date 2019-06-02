package com.fengquanwei.muse.pattern.single.single2;

/**
 * 懒汉式（线程安全：双重校验锁）
 *
 * @author fengquanwei
 * @create 2019/6/2 下午2:36
 **/
public class Singleton {
    /**
     * volatile：禁止指令重排
     */
    private volatile static Singleton INSTANCE;

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

            synchronized (Singleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
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
