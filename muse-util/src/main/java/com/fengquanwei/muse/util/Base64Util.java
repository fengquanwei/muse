package com.fengquanwei.muse.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * BASE64 工具类
 *
 * @author fengquanwei
 * @create 2018/12/10 16:35
 **/
public class Base64Util {
    /**
     * BASE64 编码
     */
    public static String encode(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        return encode(data, StandardCharsets.UTF_8);
    }

    /**
     * BASE64 编码
     */
    public static String encode(String data, Charset charset) {
        if (data == null || data.length() == 0) {
            return null;
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;

        return encode(data.getBytes(charset));
    }

    /**
     * BASE64 编码
     */
    public static String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(data);
    }

    /**
     * BASE64 编码
     */
    public static String encodeBuffer(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encodeBuffer(data);
    }

    /**
     * BASE64 解码
     */
    public static String decodeToString(String encode) {
        if (encode == null || encode.length() == 0) {
            return null;
        }

        return decodeToString(encode, StandardCharsets.UTF_8);
    }

    /**
     * BASE64 解码
     */
    public static String decodeToString(String encode, Charset charset) {
        if (encode == null || encode.length() == 0) {
            return null;
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;

        return new String(decode(encode), charset);
    }

    /**
     * BASE64 解码
     */
    public static byte[] decode(String encode) {
        if (encode == null || encode.length() == 0) {
            return null;
        }

        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        String data = "你好";

        // 编码
        String encode = encode(data);
        System.out.println(encode);

        // 解码
        String decode = decodeToString(encode);
        System.out.println(decode);
    }
}
