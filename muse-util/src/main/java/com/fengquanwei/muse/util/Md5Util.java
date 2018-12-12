package com.fengquanwei.muse.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author fengquanwei
 * @create 2018/12/10 16:32
 **/
public class Md5Util {
    /**
     * MD5 摘要（转为 16 进制，使用小写字母 a-e）
     */
    public static String md5ToString(String message) {
        if (message == null || message.length() == 0) {
            return null;
        }

        byte[] bytes = md5(message);
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
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

    /**
     * MD5 摘要
     */
    public static byte[] md5(String message) {
        if (message == null || message.length() == 0) {
            return null;
        }

        return md5(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5 摘要
     */
    public static byte[] md5(byte[] message) {
        if (message == null || message.length == 0) {
            return null;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        String message = "hello world";
        System.out.println(md5ToString(message));
    }
}
