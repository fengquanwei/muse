package com.fengquanwei.muse.util.google.guava.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 不可变集合用法
 *
 * @author fengquanwei
 * @create 2018/11/29 15:37
 **/
public class ImmutableUsage {
    public static void main(String[] args) {
        ImmutableSet<Integer> immutableSet = ImmutableSet.of(1, 2, 3);
        System.out.println(immutableSet);

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        ImmutableList<Integer> immutableList = ImmutableList.copyOf(integerList);
        System.out.println(immutableList);

        ImmutableMap<String, Object> immutableMap = ImmutableMap.of("age", 1, "name", "Tom");
        System.out.println(immutableMap);
    }
}
