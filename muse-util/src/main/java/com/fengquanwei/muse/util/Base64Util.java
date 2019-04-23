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
     * URL 安全的 BASE64 编码
     */
    public static String urlSafeEncode(byte[] data) {
        String encode = encode(data);

        if (encode == null || encode.length() == 0) {
            return null;
        }

        return encode.replaceAll("\\+", "-").replaceAll("/", "_").replaceAll("=", "\\.");
    }

    /**
     * BASE64 编码
     */
    public static String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encodeBuffer(data);
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
     * URL 安全的 BASE64 解码
     */
    public static byte[] urlSafeDecode(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }

        data = data.replaceAll("-", "\\+").replaceAll("_", "/").replaceAll("\\.", "=");

        return decode(data);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = ("URL 安全的编码！").getBytes(StandardCharsets.UTF_8);

        // 编码
        String encodeString = Base64Util.urlSafeEncode(data);
        System.out.println(encode(data));
        System.out.println(encodeString);

        // 解码
        byte[] decodeBytes = Base64Util.urlSafeDecode(encodeString);
        System.out.println(new String(decodeBytes, StandardCharsets.UTF_8));
    }
}
