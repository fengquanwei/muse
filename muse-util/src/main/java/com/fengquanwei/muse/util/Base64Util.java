package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * BASE64 工具类
 *
 * @author fengquanwei
 * @create 2018/12/10 16:35
 **/
public class Base64Util {
    private static Logger logger = LoggerFactory.getLogger(Base64Util.class);

    /**
     * BASE64 编码
     */
    public static String encode(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        return encode(data.getBytes(StandardCharsets.UTF_8));
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
    public static String decodeToString(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        return new String(decode(data), StandardCharsets.UTF_8);
    }

    /**
     * BASE64 解码
     */
    public static byte[] decode(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(data);
        } catch (IOException e) {
            logger.error("decode error", e);
            return null;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        String data = "hello world";

        // 编码
        String encode = encode(data);
        System.out.println(encode);

        // 解码
        String decode = decodeToString(encode);
        System.out.println(decode);
    }
}
