package com.fengquanwei.muse.pattern.single.single5;

/**
 * 枚举
 *
 * @author fengquanwei
 * @create 2019/6/2 下午3:05
 **/
public enum Singleton {
    INSTANCE;

    /**
     * 测试
     */
    public static void main(String[] args) {
        int threadSize = 10;

        Thread[] threads = new Thread[threadSize];

        for (int i = 0; i < threadSize; i++) {
            threads[i] = new Thread(() -> System.out.println(Singleton.INSTANCE));
        }

        for (int i = 0; i < threadSize; i++) {
            threads[i].start();
        }
    }
}
