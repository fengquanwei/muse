package com.fengquanwei.muse.util;

import java.nio.charset.StandardCharsets;

/**
 * XXTea 工具类
 *
 * @author fengquanwei
 * @create 2018/12/12 11:12
 **/
public class XxTeaUtil {
    private static final int DELTA = 0x9E3779B9;

    /**
     * XXTea 加密
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }

        return toByteArray(encrypt(toIntArray(data, true), toIntArray(fixKey(key), false)), false);
    }

    private static int[] encrypt(int[] data, int[] key) {
        int n = data.length - 1;

        if (n < 1) {
            return data;
        }

        int p, q = 6 + 52 / (n + 1);
        int z = data[n], y, sum = 0, e;

        while (q-- > 0) {
            sum = sum + DELTA;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = data[p + 1];
                z = data[p] += MX(sum, y, z, p, e, key);
            }
            y = data[0];
            z = data[n] += MX(sum, y, z, p, e, key);
        }

        return data;
    }

    /**
     * XXTea 解密
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }

        return toByteArray(decrypt(toIntArray(data, false), toIntArray(fixKey(key), false)), true);
    }

    private static int[] decrypt(int[] data, int[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }

        int n = data.length - 1;

        if (n < 1) {
            return data;
        }

        int p, q = 6 + 52 / (n + 1);
        int z, y = data[0], sum = q * DELTA, e;

        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = data[p - 1];
                y = data[p] -= MX(sum, y, z, p, e, key);
            }
            z = data[n];
            y = data[0] -= MX(sum, y, z, p, e, key);
            sum = sum - DELTA;
        }

        return data;
    }

    private static int MX(int sum, int y, int z, int p, int e, int[] k) {
        return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
    }

    private static byte[] fixKey(byte[] key) {
        if (key == null || key.length == 0) {
            return null;
        }

        if (key.length == 16) {
            return key;
        }

        byte[] fix = new byte[16];
        if (key.length < 16) {
            System.arraycopy(key, 0, fix, 0, key.length);
        } else {
            System.arraycopy(key, 0, fix, 0, 16);
        }

        return fix;
    }

    private static int[] toIntArray(byte[] data, boolean include) {
        if (data == null || data.length == 0) {
            return null;
        }

        int[] result;

        int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));

        if (include) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }

        n = data.length;

        for (int i = 0; i < n; ++i) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }

        return result;
    }

    private static byte[] toByteArray(int[] data, boolean include) {
        if (data == null || data.length == 0) {
            return null;
        }

        int n = data.length << 2;

        if (include) {
            int m = data[data.length - 1];
            n -= 4;
            if ((m < n - 3) || (m > n)) {
                return null;
            }
            n = m;
        }

        byte[] result = new byte[n];

        for (int i = 0; i < n; ++i) {
            result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
        }

        return result;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] key = "123456".getBytes(StandardCharsets.UTF_8);

        byte[] encryptBytes = encrypt(data, key);
        System.out.println(HexUtil.toHexString(encryptBytes));
        System.out.println(Base64Util.encode(encryptBytes));

        byte[] decryptBytes = decrypt(encryptBytes, key);
        System.out.println(new String(decryptBytes, StandardCharsets.UTF_8));
    }
}
