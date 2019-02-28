package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最长公共子序列问题
 * 【问题描述】
 * 给定两个序列 X = <x1, x2, ···, xm> 和 Y = <y1, y2, ···, yn> ，
 * 求 X 和 Y 的最长公共子序列
 * 【问题建模】
 * X 序列的第 i 前缀：Xi
 * Xi 和 Yj 的 LCS 的长度：c[i, j]
 * 子问题的选择：b[i, j]
 * c[i, j] = 0 (i == 0 || j == 0)
 * c[i, j] = c[i-1, j-1] + 1 (i > 0 && j > 0 && xi == yi)
 * c[i, j] = max{c[i, j-1], c[i-1, j]} (i > 0 && j > 0 && xi != yj)
 *
 * @author fengquanwei
 * @create 2019/2/28 21:57
 **/
public class LongestCommonSubSequence {
    private static final Logger logger = LoggerFactory.getLogger(LongestCommonSubSequence.class);

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(m*n)
     */
    public static void lcs(char[] X, char[] Y, int[][] c, int[][] b) {
        for (int i = 1, rows = c.length; i < rows; i++) {
            for (int j = 1, columns = c[0].length; j < columns; j++) {
                if (X[i - 1] == Y[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                    b[i][j] = 0; // 选中
                } else if (c[i - 1][j] >= c[i][j - 1]) {
                    c[i][j] = c[i - 1][j];
                    b[i][j] = 1; // 向上选
                } else {
                    c[i][j] = c[i][j - 1];
                    b[i][j] = -1; // 向左选
                }
            }
        }
    }

    /**
     * 获取 LCS
     */
    public static void getLcs(int[][] b, char[] X, int i, int j, StringBuffer lcs) {
        if (i == 0 || j == 0) {
            return;
        }

        if (b[i][j] == 0) {
            lcs.append(String.valueOf(X[i - 1]));
            getLcs(b, X, i - 1, j - 1, lcs);
        } else if (b[i][j] > 0) {
            getLcs(b, X, i - 1, j, lcs);
        } else {
            getLcs(b, X, i, j - 1, lcs);
        }
    }

    /**
     * 打印数组
     */
    public static void printArray(char[] X, char[] Y, int[][] ints, boolean arrows) {
        for (int i = 0; i < ints.length; i++) {
            StringBuffer line = new StringBuffer();
            for (int j = 0; j < ints[i].length; j++) {
                if (i == 0 && j > 0) {
                    line.append(Y[j - 1] + "\t");
                } else if (j == 0 && i > 0) {
                    line.append(X[i - 1] + "\t");
                } else {
                    // 打印箭头
                    if (arrows) {
                        int k = ints[i][j];
                        char ch = ' ';
                        if (k == 0) {
                            ch = '↖';
                        } else if (k == 1) {
                            ch = '↑';
                        } else {
                            ch = '←';
                        }
                        line.append(ch + "\t");
                    } else { // 打印 LCS 长度
                        line.append(ints[i][j] + "\t");
                    }
                }
            }
            logger.info(line.toString());
        }
    }

    public static void main(String[] args) {
        char[] X = "ABCBDAB".toCharArray();
        char[] Y = "BDCABA".toCharArray();

        int m = X.length;
        int n = Y.length;

        int[][] c = new int[m + 1][n + 1];
        int[][] b = new int[m + 1][n + 1];

        logger.info("X: {}", new String(X));
        logger.info("Y: {}", new String(Y));

        // LCS
        lcs(X, Y, c, b);
        logger.info("LCS length: {}", c[m][n]);

        StringBuffer lcs = new StringBuffer();
        getLcs(b, X, m, n, lcs);
        logger.info("LCS: {}", lcs.reverse()); // 倒序打印

        logger.info("======================= lcs length =======================");
        printArray(X, Y, c, false);
        logger.info("======================= lcs =======================");
        printArray(X, Y, b, true);
    }
}
