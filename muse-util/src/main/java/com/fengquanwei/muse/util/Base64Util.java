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
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

        // 编码
        String encodeString = encode(data);
        System.out.println(encodeString);

        // 解码
        byte[] decodeBytes = decode(encodeString);
        System.out.println(new String(decodeBytes, StandardCharsets.UTF_8));
    }
}
