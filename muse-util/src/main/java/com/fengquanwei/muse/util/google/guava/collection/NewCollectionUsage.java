package com.fengquanwei.muse.util.google.guava.collection;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 新集合用法
 *
 * @author fengquanwei
 * @create 2018/12/10 18:08
 **/
public class NewCollectionUsage {
    public static void main(String[] args) {
        System.out.println("========== Multiset ==========");
        Multiset<Integer> multiset = HashMultiset.create();

        for (int i = 0; i < 10; i++) {
            multiset.add(i / 3);
        }

        System.out.println(multiset);
        System.out.println(multiset.size());
        System.out.println(multiset.elementSet());
        System.out.println(multiset.count(1));

        System.out.println("========== Multimap ==========");
        Multimap<String, String> multimap = ArrayListMultimap.create();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multimap.put("K" + i, "V" + i);
            list.add("V" + i);
        }

        multimap.putAll("all", list);

        System.out.println(multimap);

        System.out.println("========== BiMap ==========");
        BiMap<String, String> biMap = HashBiMap.create();

        for (int i = 0; i < 10; i++) {
            biMap.put("K" + i, "V" + i);
        }

        System.out.println(biMap);
        System.out.println(biMap.inverse());

        System.out.println("========== Table ==========");
        Table<String, String, Integer> table = HashBasedTable.create();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                table.put("R" + i, "C" + j, i * j);
            }
        }

        for (int i = 0; i < table.rowKeySet().size(); i++) {
            for (int j = 0; j < table.columnKeySet().size(); j++) {
                System.out.print(table.get("R" + i, "C" + j) + "\t");
            }
            System.out.println();
        }

        System.out.println("========== ClassToInstanceMap ==========");
        ClassToInstanceMap<Object> classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.putInstance(String.class, "string");
        classToInstanceMap.putInstance(Integer.class, 1);
        System.out.println(classToInstanceMap);

        System.out.println("========== RangeSet ==========");
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closedOpen(11, 15));
        rangeSet.add(Range.closedOpen(15, 20));
        rangeSet.add(Range.openClosed(0, 0));
        System.out.println(rangeSet.span());
    }
}
