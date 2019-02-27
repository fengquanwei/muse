package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 矩阵链乘法问题
 * <p>
 * 【问题描述】
 * 给定 n 个矩阵的链 <A1，A2，...，An>，矩阵 Ai 的规模为 p(i-1) X p(i)（1 <= i <= n），
 * 求完全括号化方案，使得计算矩阵链乘积所需标量乘法次数最少。
 * <p>
 * 【问题建模】
 * 最小代价：m(i, j)
 * 矩阵规模：p(i - 1) X p(i)
 * 最优分割点：k
 * m(i, j) = 0 (i == j)
 * m(i, j) = min{m(i, k) + m(k + 1, j) + p(i - 1)*p(k)*p(j)} (i < j，其中 i <= k < j)
 *
 * @author fengquanwei
 * @create 2019/2/27 22:21
 **/
public class MatrixChainMultiply {
    private static final Logger logger = LoggerFactory.getLogger(MatrixChainMultiply.class);

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(n^3)
     */
    public static void matrixChainOrder(int[] p, int[][] m, int[][] s) {
        // l：矩阵链长度
        for (int l = 2; l < p.length; l++) {
            for (int i = 0; i < p.length - l; i++) {
                int j = i + l - 1;

                m[i][j] = Integer.MAX_VALUE;

                // k：最优分隔点
                for (int k = i; k < j; k++) {
                    int result = m[i][k] + m[k + 1][j] + p[i] * p[k + 1] * p[j + 1];

                    if (result < m[i][j]) {
                        m[i][j] = result;
                        s[i][j] = k;
                    }
                }
            }
        }
    }

    // 打印最优列表
    public static void printOptimizeList(int[][] ints, String name) {
        for (int i = 0; i < ints.length; i++) {
            StringBuffer line = new StringBuffer();
            for (int j = i; j < ints[i].length; j++) {
                line.append(name + "[" + i + "][" + j + "]: " + ints[i][j] + "\t");
            }
            logger.info(line.toString());
        }
    }

    // 获取完全括号化方案
    public static void getParenthesizedPlan(String name, int[][] s, int i, int j, StringBuffer plan) {
        if (i == j) {
            plan.append(name.charAt(i));
        } else {
            plan.append("(");
            getParenthesizedPlan(name, s, i, s[i][j], plan);
            getParenthesizedPlan(name, s, s[i][j] + 1, j, plan);
            plan.append(")");
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 矩阵规模
        int[] p = {30, 35, 15, 5, 10, 20, 25};
        // 矩阵数量
        int n = p.length - 1;
        // 最优值
        int[][] m = new int[n][n];
        // 最优解
        int[][] s = new int[n][n];
        // 矩阵名称
        String name = "ABCDEF";

        // 求解
        matrixChainOrder(p, m, s);

        // 打印最优值
        logger.info("==================== Optimum Value List ====================");
        printOptimizeList(m, "m");

        // 打印最优解
        logger.info("==================== Optimum Solution List ====================");
        printOptimizeList(s, "s");

        // 打印完全括号化方案
        StringBuffer plan = new StringBuffer();
        getParenthesizedPlan(name, s, 0, n - 1, plan);
        logger.info("==================== Fully parenthesized plan ====================");
        logger.info(plan.toString());
    }
}
