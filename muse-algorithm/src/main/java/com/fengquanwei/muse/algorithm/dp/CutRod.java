package com.fengquanwei.muse.algorithm.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 钢条切割问题
 * <p>
 * 问题描述：
 * 给定一段长度为 n 的钢条和一个价格表，求钢条切割方案，使得收益最大。
 * 长度  1  2  3  4  5  6  7  8  9 10
 * 价格 01 05 08 09 10 17 17 20 24 30
 * <p>
 * 问题建模：
 * f(0) = 0
 * f(n) = max(f(1) + f(n - 1), f(2) + f(n - 2), ..., f(n - 1) + f(1), p(n))
 * f(n) = max(p(1) + f(n - 1), p(2) + f(n - 2), ..., p(n - 1) + f(1), p(n) + f(0))
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
    public static int cutRod1(int n, Map<Integer, Integer> p) {
        if (n <= 0) {
            return 0;
        }

        int result = 0;
        int length = n <= p.size() ? n : p.size();

        for (int i = 1; i <= length; i++) {
            int subResult = p.get(i) + cutRod1(n - i, p);
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
    public static int cutRod2(int n, Map<Integer, Integer> p, Map<Integer, Integer> cache) {
        if (n <= 0) {
            return 0;
        }

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        int result = 0;
        int length = n <= p.size() ? n : p.size();

        for (int i = 1; i <= length; i++) {
            int subResult = p.get(i) + cutRod2(n - i, p, cache);
            if (result < subResult) {
                result = subResult;
            }
        }

        cache.put(n, result);
        return result;
    }

    /**
     * 自底向上的动态规划算法
     * 时间复杂度：O(n^2)
     */
    public static int cutRod3(int n, Map<Integer, Integer> p, Map<Integer, Integer> cache) {
        cache.put(0, 0);

        for (int i = 1; i <= n; i++) {
            int result = 0;
            int length = i <= p.size() ? i : p.size();

            for (int j = 1; j <= length; j++) {
                int subResult = p.get(j) + cache.get(i - j);
                if (result < subResult) {
                    result = subResult;
                }
            }

            cache.put(i, result);
        }

        return cache.get(n);
    }

    /**
     * 自底向上的动态规划算法（带方案）
     * 时间复杂度：O(n^2)
     */
    public static int cutRod4(int n, Map<Integer, Integer> p, Map<Integer, Integer> cache, Map<Integer, Integer> solution) {
        cache.put(0, 0);

        for (int i = 1; i <= n; i++) {
            int result = 0;
            int length = i <= p.size() ? i : p.size();

            for (int j = 1; j <= length; j++) {
                int subResult = p.get(j) + cache.get(i - j);
                if (result < subResult) {
                    result = subResult;
                    solution.put(i, j);
                }
            }

            cache.put(i, result);
        }

        // 打印切割方案
        printCutRodSolution(n, solution);

        return cache.get(n);
    }

    // 打印切割方案
    public static void printCutRodSolution(int n, Map<Integer, Integer> solution) {
        if (solution != null && solution.size() > 0) {
            StringBuffer result = new StringBuffer();
            result.append("cut solution: ").append(n).append(" = ");
            while (n > 0) { // 递归打印
                result.append(solution.get(n)).append(" + ");
                n = n - solution.get(n);
            }
            System.out.println();
            logger.info(result.toString().substring(0, result.length() - 2));
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 价格表
        Map<Integer, Integer> p = new HashMap<>();
        p.put(1, 1);
        p.put(2, 5);
        p.put(3, 8);
        p.put(4, 9);
        p.put(5, 10);
        p.put(6, 17);
        p.put(7, 17);
        p.put(8, 20);
        p.put(9, 24);
        p.put(10, 30);

        for (int i = 1; i <= 100; i++) {
//            logger.info("n: {}, incoming: {}", i, cutRod1(i, p));
//            logger.info("n: {}, incoming: {}", i, cutRod2(i, p, new HashMap<>()));
//            logger.info("n: {}, incoming: {}", i, cutRod3(i, p, new HashMap<>()));
            logger.info("n: {}, incoming: {}", i, cutRod4(i, p, new HashMap<>(), new HashMap<>()));
        }
    }
}
