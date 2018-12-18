package com.fengquanwei.muse.util;

/**
 * 数字工具类
 *
 * @author fengquanwei
 * @create 2018/12/17 16:20
 **/
public class NumberUtil {
    /**
     * Integer 转二进制
     */
    public static String integerToBinary(Integer number) {
        if (number == null) {
            return null;
        }

        StringBuffer binary = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            binary.append((number & (0x80000000 >>> i)) >>> (31 - i));
        }

        return binary.toString();
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(NumberUtil.integerToBinary(123456));
    }
}
