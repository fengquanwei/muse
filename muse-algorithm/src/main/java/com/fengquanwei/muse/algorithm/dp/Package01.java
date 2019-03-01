package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 01 背包问题
 * 【问题描述】
 * 有 m 件物品和一个重量为 n 的背包。第 i 件物品的重量是 w[i]，价值是 p[i]。
 * 求解将哪些物品装入背包可使价值总和最大。
 * 【问题建模】
 * 前 i 件物品放入容量为 j 的背包可以获得的最大价值：c[i][j]
 * c[i, j] = 0 (i == 0 || j ==0)
 * c[i][j] = c[i - 1][j] (j < w[i])
 * c[i][j] = max{c[i - 1][j], c[i - 1][j - w[i]] + p[i]} (j >= w[i])
 *
 * @author fengquanwei
 * @create 2019/2/28 22:57
 **/
public class Package01 {
    private static final Logger logger = LoggerFactory.getLogger(Package01.class);

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(m*n)
     */
    public static int[][] package01(int m, int n, int w[], int p[]) {
        int[][] c = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) { // 物品
            for (int j = 1; j <= n; j++) { // 重量
                if (j >= w[i - 1]) {
                    c[i][j] = max(c[i - 1][j], c[i - 1][j - w[i - 1]] + p[i - 1]);
                } else {
                    c[i][j] = c[i - 1][j];
                }
            }
        }

        return c;
    }

    private static int max(int x, int y) {
        return x > y ? x : y;
    }

    /**
     * 打印最优解（逆推法）
     */
    public static void printPlan(int c[][], int w[], int p[], int m, int n) {
        // 物品选择
        int x[] = new int[m];

        StringBuffer weight = new StringBuffer("Weight: " + n + " >= ");
        StringBuffer value = new StringBuffer("Value: " + c[m][n] + " = ");

        // 从最后一个状态记录 c[m][n] 开始逆推
        for (int i = m; i > 0; i--) {
            // 如果 c[i][n] 大于 c[i-1][n]，说明 c[i][n] 这个最优值中包含了 w[i - 1] (注意这里是 i - 1，因为 c 数组长度是 m + 1)
            if (c[i][n] > c[i - 1][n]) {
                x[i - 1] = 1;
                n -= w[i - 1];
            }
        }

        for (int i = 0; i < m; i++) {
            if (x[i] == 1) {
                weight.append(w[i]).append(" + ");
                value.append(p[i]).append(" + ");
            }
        }

        logger.info(weight.toString().substring(0, weight.length() - 2));
        logger.info(value.toString().substring(0, value.length() - 2));
    }

    /**
     * 测试
     */
    public static void main(String args[]) {
        // 物品数量
        int m = 3;
        // 背包重量
        int n = 10;
        // 物品重量
        int w[] = {3, 4, 5};
        // 物品价值
        int p[] = {4, 5, 6};

        // 计算最优值
        int c[][] = package01(m, n, w, p);

        // 打印最优解
        printPlan(c, w, p, m, n);
    }
}
