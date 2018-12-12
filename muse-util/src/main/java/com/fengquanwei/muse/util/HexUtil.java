package com.fengquanwei.muse.util;

import java.nio.charset.StandardCharsets;

/**
 * Hex 工具类
 *
 * @author fengquanwei
 * @create 2018/12/12 18:17
 **/
public class HexUtil {
    /**
     * byte[] -> string（小写字母）
     */
    public static String toHexString(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (byte b : data) {
            int n = b & 0xff;
            String h = Integer.toHexString(n);
            if (h.length() == 1) {
                result.append("0" + h);
            } else {
                result.append(h);
            }
        }

        return result.toString();
    }

    // TODO 这个应该是可逆的

    /**
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

        String hexString = toHexString(data);
        System.out.println(hexString);
    }
}
