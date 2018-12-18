package com.fengquanwei.muse.util.google.guava.basic;

import java.util.Optional;

/**
 * Optional 用法
 *
 * @author fengquanwei
 * @create 2018/11/27 18:21
 **/
public class OptionalUsage {
    public static void main(String[] args) {
        Integer a = null;
        Integer b = Optional.ofNullable(a).orElse(0);
        System.out.println(b);
    }
}
