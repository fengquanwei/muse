package com.fengquanwei.muse.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 工具类
 *
 * @author fengquanwei
 * @create 2018/12/10 16:32
 **/
public class Md5Util {
    public static String md5ToHexString(String message) {
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

    public static byte[] md5(String message) {
        if (message == null || message.length() == 0) {
            return null;
        }

        return md5(message, StandardCharsets.UTF_8);
    }

    public static byte[] md5(String message, Charset charset) {
        if (message == null || message.length() == 0) {
            return null;
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message.getBytes(charset));
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String message = "你好";
        System.out.println(md5ToHexString(message));
    }
}
