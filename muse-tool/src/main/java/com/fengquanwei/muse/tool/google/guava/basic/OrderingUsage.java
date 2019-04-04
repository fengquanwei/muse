package com.fengquanwei.muse.tool.google.guava.basic;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Ordering 用法
 *
 * @author fengquanwei
 * @create 2018/11/29 14:14
 **/
public class OrderingUsage {
    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integerList.add(i);
        }

        // 自然序比较器
        Ordering<Comparable> natural = Ordering.natural();
        System.out.println(natural.reverse().sortedCopy(integerList));

        // 自定义比较器
        integerList.add(null);
        integerList.add(null);
        integerList.add(null);
        Ordering<Integer> custom = new Ordering<Integer>() {
            @Override
            public int compare(@Nullable Integer left, @Nullable Integer right) {
                if (left == null || right == null) {
                    return 0;
                }
                return Ints.compare(left, right);
            }
        };
        System.out.println(custom.reverse().nullsFirst().sortedCopy(integerList));
    }
}
