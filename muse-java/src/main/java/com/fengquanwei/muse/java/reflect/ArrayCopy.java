package com.fengquanwei.muse.java.reflect;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 数组扩展
 *
 * @author fengquanwei
 * @create 2019/7/1 下午11:43
 **/
public class ArrayCopy {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        a = (int[]) goodCopyOf(a, 6);
        System.out.println(Arrays.toString(a));

        String[] b = {"A", "B", "C"};

        b = (String[]) goodCopyOf(b, 6);
        System.out.println(Arrays.toString(b));

        Object[] copy = badCopyOf(b, 6);

        // ClassCastException
        b = (String[]) copy;
    }

    /**
     * 数组会记住元素类型，转换会报 ClassCastException
     */
    public static Object[] badCopyOf(Object[] array, int newLength) {
        Object[] newArray = new Object[newLength];

        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, newLength));

        return newArray;
    }

    /**
     * 参数类型之所以使用 Object 而不是 Object[]，是为了支持 int[]
     */
    public static Object goodCopyOf(Object array, int newLength) {
        Class<?> clazz = array.getClass();

        if (!clazz.isArray()) {
            return null;
        }

        Class<?> componentType = clazz.getComponentType();
        Object newArray = Array.newInstance(componentType, newLength);

        int length = Array.getLength(array);
        System.arraycopy(array, 0, newArray, 0, Math.min(length, newLength));

        return newArray;
    }
}
