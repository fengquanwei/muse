package com.fengquanwei.muse.pattern.single;

/**
 * 单例模式 - 饿汉式
 *
 * @author fengquanwei
 * @create 2019/5/29 11:52
 **/
public class Singleton {
    private static final Singleton SINGLETON = new Singleton();

    private Singleton() {
    }

    public static Singleton getSingleton() {
        return SINGLETON;
    }
}
