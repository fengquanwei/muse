package com.fengquanwei.muse.util;

import javax.crypto.Cipher;
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
    /**
     * 生成并打印公私密钥对
     */
    public static void generateAndPrintKeyPair() {
        KeyPair keyPair = generateKeyPair();

        if (keyPair == null) {
            return;
        }

        PublicKey publicKey = keyPair.getPublic();
        System.out.println("========== public key start ==========");
        System.out.println(Base64Util.encodeBuffer(publicKey.getEncoded()));
        System.out.println("========== public key end ==========");

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("========== private key start ==========");
        System.out.println(Base64Util.encodeBuffer(privateKey.getEncoded()));
        System.out.println("========== private key end ==========");
    }

    /**
     * 生成公私密钥对
     */
    public static KeyPair generateKeyPair() {
        // 加密算法：RSA
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
     * 获取公钥
     */
    public static PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode(base64PublicKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用公钥加密
     */
    public static String encryptToString(String data, PublicKey publicKey) {
        if (data == null || data.length() == 0 || publicKey == null) {
            return null;
        }

        byte[] bytes = encrypt(data.getBytes(StandardCharsets.UTF_8), publicKey);

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return Base64Util.encode(bytes);
    }

    /**
     * 使用公钥加密
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        if (data == null || data.length == 0 || publicKey == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥解密
     */
    public static String decryptToString(String data, PrivateKey privateKey) {
        if (data == null || data.length() == 0 || privateKey == null) {
            return null;
        }

        byte[] bytes = decrypt(Base64Util.decode(data), privateKey);

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey) {
        if (data == null || data.length == 0 || privateKey == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 生成并打印公私密钥对
//        generateAndPrintKeyPair();

        String base64PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxLeARdFJCGhgNGUT5DUQW3qrTuvvgzQWcn7+D\n" +
                "mN2oo2ABdsGr88BGIcCtU8o1HFFFR37JijDZHx9TTl3ukNGqXEu1u7MFNVm0Jt3gWYK1EMzxV6rN\n" +
                "cjTvnspzjxRkj3GQoc3ozzJlNwl4pa86DAcvqW8zvJmkHtAIIBfF0OBfLQIDAQAB\n";

        String base64PrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALEt4BF0UkIaGA0ZRPkNRBbeqtO6\n" +
                "++DNBZyfv4OY3aijYAF2wavzwEYhwK1TyjUcUUVHfsmKMNkfH1NOXe6Q0apcS7W7swU1WbQm3eBZ\n" +
                "grUQzPFXqs1yNO+eynOPFGSPcZChzejPMmU3CXilrzoMBy+pbzO8maQe0AggF8XQ4F8tAgMBAAEC\n" +
                "gYAldPxc1EmbneadxkzVlh9h8lsM/gfH47AfB4q2aHfb8gYW6SNSlep9KR+RxRgHmF/6FH5SXzuq\n" +
                "ezDlLLkg+U7JqjZ6wpSoJ9G8fSQknQKPGrL779Qr0CbPBWP9esxeiomhq6Od3imEeHW4ESfSMMmx\n" +
                "bo0mAuTw8QLVY+2KRPUNhQJBANwHaGIegtIrr2SNBK5oln8N9xNaUK1/r19VD8uNXSkLmS34xr3r\n" +
                "37B4AizX0H3udPiipTnBHw1HdIljpCvTKGsCQQDOJSG+Lh08LTfn57Do2WkZ4i7knp13hIrMmtOg\n" +
                "waHHGqssj6ZeE908npJf77jGB2TJQIIq6goDEfI7k3EbANzHAkBxKy45KVBIIUf5A78gpZKijuBC\n" +
                "B/XvBvmAoxOYsoD0F48V4hr2nFdVQKR9xBLscrWfHKi25+m8vr/l2mYaVGU5AkBWE3EcfRrd9q+i\n" +
                "WL8o6ycUlLop4gU0U5a1SmzVciTAA2W4LJ82Jys41ame/3Ty00GFOzde/eyCTasMr0sKEHkfAkAk\n" +
                "3KCmURraCCqjYWx9rRJa03Y4ZfQdchfCzXoYLWSZIuQvPLnVidLVc7KLdm0nwYaac94Rakj5TSLG\n" +
                "SD/5IEf+\n";

        // 获取公私密钥
        PublicKey publicKey = getPublicKey(base64PublicKey);
        PrivateKey privateKey = getPrivateKey(base64PrivateKey);

        String data = "hello world";

        // 使用公钥加密
        String encrypt = encryptToString(data, publicKey);
        System.out.println(encrypt);

        // 使用私钥解密
        String decrypt = decryptToString(encrypt, privateKey);
        System.out.println(decrypt);
    }
}
