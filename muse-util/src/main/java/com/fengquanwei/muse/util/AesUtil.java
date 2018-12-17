package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES 工具类
 *
 * @author fengquanwei
 * @create 2018/12/17 16:27
 **/
public class AesUtil {
    private static Logger logger = LoggerFactory.getLogger(AesUtil.class);

    /**
     * AES 加密
     */
    public static String encrypt(String data, String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) {
            return null;
        }

        // 密钥：MD5；密文：BASE64 encode
        return Base64Util.encode(encrypt(data.getBytes(StandardCharsets.UTF_8), Md5Util.md5(key.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * AES 加密
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length != 16) {
            return null;
        }

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("encrypt error", e);
            return null;
        }
    }

    /**
     * AES 解密
     */
    public static String decrypt(String data, String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) {
            return null;
        }

        // 密钥：MD5；密文：BASE64 decode
        return new String(decrypt(Base64Util.decode(data), Md5Util.md5(key.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
    }

    /**
     * AES 解密
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length != 16) {
            return null;
        }

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("decrypt error", e);
            return null;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        String data = "hello world";
        String key = "hello world";

        String encrypt = encrypt(data, key);
        System.out.println(encrypt);

        String decrypt = decrypt(encrypt, key);
        System.out.println(decrypt);
    }
}
