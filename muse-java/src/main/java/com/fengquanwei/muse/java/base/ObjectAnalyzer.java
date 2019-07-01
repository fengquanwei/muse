package com.fengquanwei.muse.java.base;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象分析器（反射的用法）
 *
 * @author fengquanwei
 * @create 2019/6/30 下午11:56
 **/
public class ObjectAnalyzer {
    /**
     * 记录已访问过的对象，防止循环引用导致无限循环问题
     */
    private List<Object> visited = new ArrayList<>();

    /**
     * 通用的 toString 方法
     */
    public String toString(Object object) {
        if (object == null) {
            return "null";
        }

        if (visited.contains(object)) {
            return "...";
        }

        visited.add(object);

        Class<?> clazz = object.getClass();

        // 字符串类型
        if (clazz == String.class) {
            return (String) object;
        }

        // 数组类型
        if (clazz.isArray()) {
            String result = clazz.getComponentType() + "[]{";

            for (int i = 0; i < Array.getLength(object); i++) {
                if (i > 0) {
                    result += ",";
                }

                Object value = Array.get(object, i);

                if (clazz.getComponentType().isPrimitive()) {
                    result += value;
                } else {
                    result += toString(value);
                }
            }

            return result + "}";
        }

        // 其他引用类型
        String result = clazz.getName();

        result += "[";

        do {
            Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);

            for (Field field : fields) {
                // 只处理非静态成员
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!result.endsWith("[")) {
                        result += ",";
                    }

                    result += field.getName() + "=";

                    try {
                        Class<?> type = field.getType();
                        Object value = field.get(object);

                        if (type.isPrimitive()) {
                            result += value;
                        } else {
                            result += toString(value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 处理父类成员
            clazz = clazz.getSuperclass();
        } while (clazz != null);

        result += "]";

        return result;
    }

    public static void main(String[] args) {
        List<Integer> squares = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            squares.add(i * i);
        }

        System.out.println(new ObjectAnalyzer().toString(squares));
    }
}
