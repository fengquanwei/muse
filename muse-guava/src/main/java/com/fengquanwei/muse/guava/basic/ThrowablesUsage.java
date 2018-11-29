package com.fengquanwei.muse.guava.basic;

import com.google.common.base.Throwables;

/**
 * Throwables 用法
 *
 * @author fengquanwei
 * @create 2018/11/29 15:20
 **/
public class ThrowablesUsage {
    public static void main(String[] args) {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }

        System.out.println("done");
    }
}
