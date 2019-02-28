package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数字三角形问题
 * 【问题描述】
 * 7
 * 3  8
 * 8  1  0
 * 2  7  4  4
 * 4  5  2  6  5
 * 在上面的数字三角形中寻找一条从顶部到底边的路径，使得路径上所经过的数字之和最大。
 * 路径上的每一步都只能往左下或右下走。
 * 【问题建模】
 * 数字三角形行数：n
 * 第 i 行第 j 列的数字：d[i, j]
 * 第 i 行第 j 列最大和：m[i ,j]
 * m[i, j] = d[i, j] (i == n)
 * m[i, j] = max{m[i + 1, j], m[i + 1, j + 1]} (i < n)
 *
 * @author fengquanwei
 * @create 2019/2/28 23:33
 **/
public class DigitalTriangle {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTriangle.class);

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(n^2)
     */
    public static void calculateMaxSum(int[][] d, int[][] m, int[] s) {
        int n = d.length - 1;

        // 最后一行
        m[n] = new int[n + 1];
        for (int j = 1; j < d[n].length; j++) {
            m[n][j] = d[n][j];
        }

        for (int i = n - 1; i >= 1; i--) {
            m[i] = new int[i + 1];
            for (int j = 1; j < d[i].length; j++) {
                if (m[i + 1][j] > m[i + 1][j + 1]) {
                    m[i][j] = d[i][j] + m[i + 1][j];
                    s[i + 1] = j;
                } else {
                    m[i][j] = d[i][j] + m[i + 1][j + 1];
                    s[i + 1] = j + 1;
                }
            }
        }
    }

    /**
     * 打印数字三角形
     */
    public static void printDigitalTriangle(int[][] ints) {
        for (int i = 1, length = ints.length; i < length; i++) {
            StringBuffer line = new StringBuffer();
            for (int k = 1; k < length - i; k++) {
                line.append("  ");
            }
            for (int j = 1, len = ints[i].length; j < len; j++) {
                if (ints[i][j] < 10) {
                    line.append("0");
                }
                line.append(ints[i][j] + "  ");

            }
            logger.info(line.toString());
        }
    }

    /**
     * 打印最优解
     */
    public static void printPlan(int[][] m) {
        int n = m.length - 1;

        StringBuffer plan = new StringBuffer();
        int j = 1;
        for (int i = 1; i <= n; i++) {
            if (i == n) {
                if (m[i][j] > m[i][j + 1]) {
                    plan.append(m[i][j] + " + ");
                } else {
                    plan.append(m[i][j + 1] + " + ");
                }
                break;
            }

            int left = m[i + 1][j];
            int right = m[i + 1][j + 1];

            if (left > right) {
                plan.append(m[i][j] - m[i + 1][j] + " + ");
            } else {
                plan.append(m[i][j] - m[i + 1][j + 1] + " + ");
                j = j + 1;
            }
        }

        logger.info(plan.toString().substring(0, plan.length() - 2));
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        int[][] d = new int[][]{
                {},
                {0, 7},
                {0, 3, 8},
                {0, 8, 1, 0},
                {0, 2, 7, 4, 4},
                {0, 4, 5, 2, 6, 5},
        };

        int[][] m = new int[d.length][];
        int[] s = new int[d.length];

        // 打印数字三角形
        logger.info("==================== d ====================");
        printDigitalTriangle(d);

        // 计算最大和
        calculateMaxSum(d, m, s);

        // 打印最大和三角形
        logger.info("==================== m ====================");
        printDigitalTriangle(m);

        // 打印最优解
        logger.info("==================== s ====================");
        printPlan(m);
    }
}
