package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 钢条切割问题
 * <p>
 * 【问题描述】
 * 给定一段长度为 n 的钢条和一个价格表，求钢条切割方案，使得收益最大。
 * 长度  1  2  3  4  5  6  7  8  9 10
 * 价格 01 05 08 09 10 17 17 20 24 30
 * <p>
 * 【问题建模】
 * 钢条长度：n
 * 钢条价格：p[i]
 * 最大收益：r[i]
 * 第一切割点：k
 * r[0] = 0
 * r[n] = max{p[k] + r[n - k]} (n >= 1, 其中 0 < k <= n)
 *
 * @author fengquanwei
 * @create 2019/2/24 17:53
 **/
public class CutRod {
    private static final Logger logger = LoggerFactory.getLogger(CutRod.class);

    /**
     * 自顶向下的递归算法
     * 时间复杂度：O(2^n)
     */
    public static int cutRod1(int n, int[] p) {
        if (n <= 0) {
            return 0;
        }

        int result = 0;
        int length = n <= p.length - 1 ? n : p.length - 1;

        for (int k = 1; k <= length; k++) {
            int subResult = p[k] + cutRod1(n - k, p);
            if (result < subResult) {
                result = subResult;
            }
        }

        return result;
    }

    /**
     * 自顶向下的递归算法（带备忘）
     * 时间复杂度：O(n^2)
     */
    public static int cutRod2(int n, int[] p, int[] cache) {
        if (n <= 0) {
            return 0;
        }

        if (cache[n] != 0) {
            return cache[n];
        }

        int result = 0;
        int length = n <= p.length - 1 ? n : p.length - 1;

        for (int k = 1; k <= length; k++) {
            int subResult = p[k] + cutRod2(n - k, p, cache);
            if (result < subResult) {
                result = subResult;
            }
        }

        cache[n] = result;
        return result;
    }

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(n^2)
     */
    public static int cutRod3(int n, int[] p, int[] cache) {
        cache[0] = 0;

        for (int i = 1; i <= n; i++) {
            int result = 0;
            int length = i <= p.length - 1 ? i : p.length - 1;

            for (int k = 1; k <= length; k++) {
                int subResult = p[k] + cache[i - k];
                if (result < subResult) {
                    result = subResult;
                }
            }

            cache[i] = result;
        }

        return cache[n];
    }

    /**
     * 自底向上的动态规划算法（带方案）
     * 时间复杂度：O(n^2)
     */
    public static int cutRod4(int n, int[] p, int[] cache, int[] s) {
        cache[0] = 0;

        for (int i = 1; i <= n; i++) {
            int result = 0;
            int length = i <= p.length - 1 ? i : p.length - 1;

            for (int k = 1; k <= length; k++) {
                int subResult = p[k] + cache[i - k];
                if (result < subResult) {
                    result = subResult;
                    s[i] = k;
                }
            }

            cache[i] = result;
        }

        // 打印切割方案
        printCutRodSolution(n, s);

        return cache[n];
    }

    // 打印切割方案
    public static void printCutRodSolution(int n, int[] s) {
        if (s != null && s.length > 0) {
            StringBuffer plan = new StringBuffer();
            plan.append("cut plan: ").append(n).append(" = ");
            while (n > 0) { // 递归打印
                plan.append(s[n]).append(" + ");
                n = n - s[n];
            }
            System.out.println();
            logger.info(plan.toString().substring(0, plan.length() - 2));
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 价格
        int[] p = new int[]{0, 1, 5, 8, 9, 10, 17, 17, 20, 24, 30};

        for (int i = 1; i <= 88; i++) {
//            logger.info("n: {}, r: {}", i, cutRod1(i, p));
//            logger.info("n: {}, r: {}", i, cutRod2(i, p, new int[i + 1]));
//            logger.info("n: {}, r: {}", i, cutRod3(i, p, new int[i + 1]));
            logger.info("n: {}, r: {}", i, cutRod4(i, p, new int[i + 1], new int[i + 1]));
        }
    }
}
