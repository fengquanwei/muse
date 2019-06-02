package com.fengquanwei.muse.pattern.single.single3;

/**
 * 饿汉式
 * 缺点：不是懒加载模式，在某些场景下无法使用（如实例的创建依赖参数或配置文件）
 *
 * @author fengquanwei
 * @create 2019/6/2 下午2:55
 **/
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
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
