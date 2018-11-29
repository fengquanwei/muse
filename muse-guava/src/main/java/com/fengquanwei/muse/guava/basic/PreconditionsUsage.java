package com.fengquanwei.muse.guava.basic;

import com.google.common.base.Preconditions;

/**
 * Preconditions 用法
 *
 * @author fengquanwei
 * @create 2018/11/28 10:18
 **/
public class PreconditionsUsage {
    public static void main(String[] args) {
        int i = -1;
        try {
            Preconditions.checkArgument(i >= 0, "Argument was %s but expected non negative", i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer num = null;
        try {
            System.out.println(Preconditions.checkNotNull(num));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
