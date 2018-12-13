package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        // 1024 位密钥最多支持 117 字节明文加密
        if (data.length > 117) {
            return null;
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
     * 解密
     */
    public static byte[] decrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }

        // 1024 位密钥最多支持 128 字节密文加密
        if (data.length > 128) {
            return null;
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

        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

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
    }
}
