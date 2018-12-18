package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(Md5Util.class);

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
            logger.error("md5 error", e);
            return null;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

        byte[] md5Bytes = Md5Util.md5(data);

        System.out.println(HexUtil.byteToHex(md5Bytes));
        System.out.println(Base64Util.encode(md5Bytes));
    }
}
