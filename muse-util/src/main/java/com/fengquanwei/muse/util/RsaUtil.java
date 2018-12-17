package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具类
 *
 * @author fengquanwei
 * @create 2018/12/11 10:37
 **/
public class RsaUtil {
    private static Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        // 1024 位密钥最多支持 117 字节明文加密，超过则分段加密
        if (data.length > 117) {
            return segmentEncrypt(data, key);
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("encrypt error", e);
            return null;
        }
    }

    /**
     * 分段加密
     */
    public static byte[] segmentEncrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int length = data.length;
            int offset = 0;
            byte[] cache;

            while (length > offset) {
                if (length - offset > 117) {
                    cache = cipher.doFinal(data, offset, 117);
                } else {
                    cache = cipher.doFinal(data, offset, length - offset);
                }
                output.write(cache, 0, cache.length);

                offset += 117;
            }

            return output.toByteArray();
        } catch (Exception e) {
            logger.error("encrypt error", e);
            return null;
        }
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        // 1024 位密钥最多支持 128 字节密文解密，超过则分段解密
        if (data.length > 128) {
            return segmentDecrypt(data, key);
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("decrypt error", e);
            return null;
        }
    }

    /**
     * 分段解密
     */
    public static byte[] segmentDecrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int length = data.length;
            int offset = 0;
            byte[] cache;

            while (length > offset) {
                if (length - offset > 128) {
                    cache = cipher.doFinal(data, offset, 128);
                } else {
                    cache = cipher.doFinal(data, offset, length - offset);
                }
                output.write(cache, 0, cache.length);

                offset += 128;
            }

            return output.toByteArray();
        } catch (Exception e) {
            logger.error("decrypt error", e);
            return null;
        }
    }

    /**
     * 使用私钥签名
     */
    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            logger.error("sign error", e);
            return null;
        }
    }

    /**
     * 使用公钥验签
     */
    public static boolean verify(byte[] data, byte[] sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            logger.error("verify error", e);
            return false;
        }
    }

    /**
     * 获取公钥
     */
    public static PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode(base64PublicKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            logger.error("getPublicKey error", e);
            return null;
        }
    }

    /**
     * 获取私钥
     */
    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Util.decode(base64PrivateKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            logger.error("getPrivateKey error", e);
            return null;
        }
    }

    /**
     * 生成并打印公私密钥对
     */
    private static void generateAndPrintKeyPair() {
        KeyPair keyPair = generateKeyPair();

        if (keyPair == null) {
            return;
        }

        PublicKey publicKey = keyPair.getPublic();
        System.out.println("========== public key start ==========");
        System.out.println(Base64Util.encode(publicKey.getEncoded()));
        System.out.println("========== public key end ==========");

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("========== private key start ==========");
        System.out.println(Base64Util.encode(privateKey.getEncoded()));
        System.out.println("========== private key end ==========");
    }

    /**
     * 生成公私密钥对
     */
    private static KeyPair generateKeyPair() {
        // 加密算法：RSA
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.error("generateKeyPair error", e);
            return null;
        }

        // 密钥长度：1024 位
        keyPairGenerator.initialize(1024, new SecureRandom());

        // 生成公私密钥对（公钥：x.509 格式，私钥：pkcs8 格式）
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 使用 openssl 生成公私密钥对
     * 1 生成私钥
     * openssl genrsa -out rsa_private_key_pkcs1.pem 1024
     * 2 根据私钥生成公钥
     * openssl rsa -in rsa_private_key_pkcs1.pem -pubout -out rsa_public_key.pub
     * 3 将私钥转换成 pkcs8 格式
     * openssl pkcs8 -topk8 -inform PEM -in rsa_private_key_pkcs1.pem -outform PEM -nocrypt > rsa_private_key.pem
     */

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 生成并打印公私密钥对
//        generateAndPrintKeyPair();

        String base64PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiT/ZH8UdeiKINaJtj37jk9Gu0+ILUez4qnE+C\n" +
                "qKX4put3KUi8sH12zH5GG9TVy/avWxmqmMMQk3htGogW9zi6n2mclRVT8+eUTgSNK6yZbJokt9Fv\n" +
                "qDlYsvs38TkXqEe5DJBmeW6JlSERsphkLsEm006cF/3zmsjDNJuOVkYacQIDAQAB\n";

        String base64PrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOJP9kfxR16Iog1om2PfuOT0a7T4\n" +
                "gtR7PiqcT4Kopfim63cpSLywfXbMfkYb1NXL9q9bGaqYwxCTeG0aiBb3OLqfaZyVFVPz55ROBI0r\n" +
                "rJlsmiS30W+oOViy+zfxOReoR7kMkGZ5bomVIRGymGQuwSbTTpwX/fOayMM0m45WRhpxAgMBAAEC\n" +
                "gYAGwEHlhYIOhN7orX0tLohSdCQeYudsNTz2gnEwwZ0p3LjbdiTQVCd5+sGtj7j6o7n5IyuR1jrD\n" +
                "bNLD1m3G2Zscj7TDl+hhJIXs1EzaaJRnUUUKwf4SNDPGa0rWKbT8DDp7DH8z2EwX0gJZXk5HzG5g\n" +
                "+O8Kzao3/sYxkeVnEZxpLQJBAPhFa3GrytN5mW+MSVnYqLCV2kHhybjJPj7vs7mKPBebILi1wkHc\n" +
                "6JteXI52GIWSi6FA66gwhcfv5mCJa+RwrY8CQQDpW4sCreINTXWJQ+yvBVISAFvfPUYL/KiFbWeZ\n" +
                "nI6r+QgHFi1iInCkn5CFeRlqTfsB3QL4o3CTDTIxkfFWiLf/AkEA22DMHjeFE/ThJfY6Yo/WD6N6\n" +
                "priS/RumSwqYLxz9GIbVFSJWssg3KgLpmmC0LmNdynGJSqcFr7RGR4tmsQb39QJAFIfvVQ8DYnQ8\n" +
                "MRV+z29CZz8M1jlH3moeUY7snIE/tDW21RjIaWfqTTGUv4mGCOpPeTx+tG18zBNPJTlX6Gsi9QJB\n" +
                "ALnv7tQ2UE0MzjQAbQZE//xYYcTUUUUO7DiFTX7uGItzau+nT7cbmrUqpo9lLTMJgBTdo2u3a/SE\n" +
                "hYMqUdiPAAc=\n";

        // 获取公私密钥
        PublicKey publicKey = getPublicKey(base64PublicKey);
        PrivateKey privateKey = getPrivateKey(base64PrivateKey);

        byte[] data = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九".getBytes(StandardCharsets.UTF_8);

        // 使用公钥加密
        byte[] encryptBytes = encrypt(data, publicKey);
        System.out.println(HexUtil.byteToHex(encryptBytes));
        System.out.println(Base64Util.encode(encryptBytes));

        // 使用私钥解密
        byte[] decryptBytes = decrypt(encryptBytes, privateKey);
        System.out.println(new String(decryptBytes, StandardCharsets.UTF_8));

        // 使用私钥加密
        encryptBytes = encrypt(data, privateKey);
        System.out.println(HexUtil.byteToHex(encryptBytes));
        System.out.println(Base64Util.encode(encryptBytes));

        // 使用公钥解密
        decryptBytes = decrypt(encryptBytes, publicKey);
        System.out.println(new String(decryptBytes, StandardCharsets.UTF_8));

        // 使用私钥签名
        byte[] sign = sign(data, privateKey);
        System.out.println(HexUtil.byteToHex(sign));

        // 使用公钥验签
        boolean verify = verify(data, sign, publicKey);
        System.out.println(verify);
    }
}
