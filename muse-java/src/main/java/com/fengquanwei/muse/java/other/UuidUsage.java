package com.fengquanwei.muse.java.other;

import java.util.UUID;

/**
 * UUID 用法
 *
 * @author fengquanwei
 * @create 2019/4/4 22:41
 **/
public class UuidUsage {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println(uuid);
        }
    }
}
