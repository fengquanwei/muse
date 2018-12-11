package com.fengquanwei.muse.util;

import javax.crypto.Cipher;
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
    public static byte[] decrypt(byte[] encrypt, PrivateKey privateKey) {
        if (encrypt == null || encrypt.length == 0 || privateKey == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encrypt);
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

        String base64PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcp/mwRHYzSwum74xw4X+LHEZ0jY11OpS87397\n" +
                "RsN+Jk5m0mzZ91RIKuOvlS3RMENyTE2FQPdiEMz2b9EhIuPoMWq1EDLiP9BWXRY7a2DZt8qWPexj\n" +
                "GEHju3Q11hKZLBYYZNQ6BdsPjjTwb7l2AdT9vdaaUpOFBtGPBUjMK35B0wIDAQAB\n";

        String base64PrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJyn+bBEdjNLC6bvjHDhf4scRnSN\n" +
                "jXU6lLzvf3tGw34mTmbSbNn3VEgq46+VLdEwQ3JMTYVA92IQzPZv0SEi4+gxarUQMuI/0FZdFjtr\n" +
                "YNm3ypY97GMYQeO7dDXWEpksFhhk1DoF2w+ONPBvuXYB1P291ppSk4UG0Y8FSMwrfkHTAgMBAAEC\n" +
                "gYBexIcaCPBway+rVzLgfqnHn91HWPKAPmYIPeIi01YkFExNi3JqEWMdHUULzPUgnU/P7zTeLcT5\n" +
                "wCwd0Nr0bsTrl56jEObFa61IkzN8vdLBsa6LpcRAYwsI4ffclsB1THSYKBGN5qPKMdj/PVeRNW4i\n" +
                "Oe7Effr47hmvqz9CHEI9cQJBAOV10I6SYaxVyH9XRmYTR6x+lDdRkRxtM4IJ5tN8RkcFY8ShgPb1\n" +
                "AnYMqSdUJSNbTI4chCeaEWLF88OjSSUbQo8CQQCuxnll8JTpx8euK1WPOxstm89FR0okkPQRyhcg\n" +
                "Qeppim6F0OxToWgDaMc/kfGyewmVlqJVssdiD+8q32XXfh59AkEAkusukDr1wwxiBfbxomXx3GZn\n" +
                "rEvTp+nbswV4AC1wKgUvbjUih/00iDVvETl1VgAdMljb3SduvlmUzMz+Bn4/zQJAM3g3oBGtiPtQ\n" +
                "jrYsSDX71v6fl6Na7lHRzrtY/CLtSY9+5OGV9zr9SqO8qbftGcaqF+d4fwxbM0HGCTYo48E8cQJB\n" +
                "AIW4NymL9tfrNevZlu2H6Bo0gpsRIBzHP+AexwIKcW0bAxSPiR48y1W0dTVnJgc7zOx1wgYJhCJi\n" +
                "OqV+zNchIys=\n";

        // 获取公私密钥
        PublicKey publicKey = getPublicKey(base64PublicKey);
        PrivateKey privateKey = getPrivateKey(base64PrivateKey);

        String data = "hello";

        // 使用公钥加密
        byte[] encrypt = encrypt(data.getBytes(), publicKey);

        // 使用私钥解密
        byte[] decrypt = decrypt(encrypt, privateKey);
        System.out.println("decrypt: " + new String(decrypt));

    }
}
