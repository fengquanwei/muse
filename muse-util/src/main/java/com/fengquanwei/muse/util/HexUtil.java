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
     * 字节数组转十六进制
     */
    public static String byteToHex(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        for (byte b : data) {
            String hex = Integer.toHexString(b & 0xff);

            if (hex.length() == 1) {
                result.append("0");
            }

            result.append(hex);
        }

        return result.toString();
    }

    /**
     * 十六进制转字节数组
     */
    public static byte[] hexToByte(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        int length = data.length();

        byte[] result = new byte[length / 2];

        int j = 0;
        for (int i = 0; i < length; i += 2) {
            result[j++] = (byte) Integer.parseInt(data.substring(i, i + 2), 16);
        }

        return result;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

        String hexString = HexUtil.byteToHex(data);
        System.out.println(hexString);

        byte[] bytes = HexUtil.hexToByte(hexString);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }
}
