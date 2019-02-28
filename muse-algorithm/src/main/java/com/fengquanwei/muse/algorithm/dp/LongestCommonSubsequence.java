package com.fengquanwei.muse.algorithm.dp;

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
public class LongestCommonSubsequence {
    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(m*n)
     */
    public static void lcs(char[] X, char[] Y, int[][] c, int[][] b) {
        for (int i = 1, rows = c.length; i < rows; i++) {
            for (int j = 1, columns = c[0].length; j < columns; j++) {
                if (X[i - 1] == Y[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                    b[i][j] = 0;
                } else if (c[i - 1][j] >= c[i][j - 1]) {
                    c[i][j] = c[i - 1][j];
                    b[i][j] = 1;
                } else {
                    c[i][j] = c[i][j - 1];
                    b[i][j] = -1;
                }
            }
        }
    }

    /**
     * 打印 LCS
     */
    public static void printLcs(int[][] b, char[] X, int i, int j, StringBuffer sb) {
        if (i == 0 || j == 0) {
            return;
        }

        if (b[i][j] == 0) {
            sb.append(String.valueOf(X[i - 1]));
            printLcs(b, X, i - 1, j - 1, sb);
        } else if (b[i][j] > 0) {
            printLcs(b, X, i - 1, j, sb);
        } else {
            printLcs(b, X, i, j - 1, sb);
        }
    }

    public static void printArray(char[] X, char[] Y, int[][] c, boolean arrows) {
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                if (i == 0 && j > 0) {
                    System.out.print(Y[j - 1] + "\t");
                } else if (j == 0 && i > 0) {
                    System.out.print(X[i - 1] + "\t");
                } else {
                    if (arrows) {
                        int k = c[i][j];
                        char ch = ' ';
                        if (k == 0) {
                            ch = '↖';
                        } else if (k == 1) {
                            ch = '↑';
                        } else {
                            ch = '←';
                        }
                        System.out.print(ch + "\t");
                    } else {
                        System.out.print(c[i][j] + "\t");
                    }
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[] X = "ABCBDAB".toCharArray();
        char[] Y = "BDCABA".toCharArray();

        int m = X.length;
        int n = Y.length;

        int[][] c = new int[m + 1][n + 1];
        int[][] b = new int[m + 1][n + 1];

        // LCS
        lcs(X, Y, c, b);
        System.out.println("LCS length: " + c[m][n]);

        StringBuffer sb = new StringBuffer();
        printLcs(b, X, m, n, sb);
        System.out.println("LCS: " + sb.reverse()); // 倒序打印

        System.out.println("======================= c =======================");
        printArray(X, Y, c, false);
        System.out.println("======================= b =======================");
        printArray(X, Y, b, true);
    }
}
