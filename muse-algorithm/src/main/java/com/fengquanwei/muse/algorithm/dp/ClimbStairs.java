package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬楼梯问题
 * <p>
 * 【问题描述】
 * 有一座高度是 10 级台阶的楼梯，从下往上走，每跨一步只能向上 1 级或者 2 级台阶。
 * 要求用程序求出一共有多少种走法。
 * <p>
 * 【问题建模】
 * 台阶级数：n
 * 总共走法：f(n)
 * f(1) = 1
 * f(2) = 2
 * f(n) = f(n - 1) + f(n - 2) (n >= 3)
 *
 * @author fengquanwei
 * @create 2019/2/22 00:41
 **/
public class ClimbStairs {
    private static final Logger logger = LoggerFactory.getLogger(ClimbStairs.class);

    /**
     * 自顶向下的递归算法
     * 时间复杂度：O(2^n)
     */
    public static long getClimbingWays1(int n) {
        if (n < 1) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        if (n == 2) {
            return 2;
        }

        return getClimbingWays1(n - 1) + getClimbingWays1(n - 2);
    }

    /**
     * 自顶向下的递归算法（带忘录）
     * 时间复杂度：O(n)
     */
    public static long getClimbingWays2(int n, long[] cache) {
        if (n < 1) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        if (n == 2) {
            return 2;
        }

        if (cache[n] != 0) {
            return cache[n];
        }

        long result = getClimbingWays2(n - 1, cache) + getClimbingWays2(n - 2, cache);

        cache[n] = result;
        return result;
    }

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(n)，空间复杂度：0(1)
     */
    public static long getClimbingWays3(int n) {
        if (n < 1) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        if (n == 2) {
            return 2;
        }

        long a = 1;
        long b = 2;

        long result = 0;

        for (int i = 3; i <= n; i++) {
            result = a + b;
            a = b;
            b = result;
        }

        return result;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        for (int i = 1; i <= 88; i++) {
//            logger.info("n: {}, f: {}", i, getClimbingWays1(i));
//            logger.info("n: {}, f: {}", i, getClimbingWays2(i, new long[i + 1]));
            logger.info("n: {}, f: {}", i, getClimbingWays3(i));
        }
    }
}
