package com.fengquanwei.muse.pattern.single.single4;

/**
 * 静态内部类
 *
 * @author fengquanwei
 * @create 2019/6/2 下午3:01
 **/
public class Singleton {
    private Singleton() {
    }

    /**
     * 私有静态内部类持有单例实例
     */
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static final Singleton getInstance() {
        return SingletonHolder.INSTANCE;
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
