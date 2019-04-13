package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增长工具类
 * http://zuotu.91maths.com/#W3sidHlwZSI6MCwiZXEiOiJ4IiwiY29sb3IiOiIjMDAwMDAwIn0seyJ0eXBlIjowLCJlcSI6InheMiIsImNvbG9yIjoiIzAwMDAwMCJ9LHsidHlwZSI6MCwiZXEiOiJsb2coMTAweCkvMiIsImNvbG9yIjoiIzAwMDAwMCJ9LHsidHlwZSI6MTAwMCwid2luZG93IjpbIi0wLjYyMTQ1OTk3ODY0OTU5OSIsIjEuNTU5NTc4MTAxMzUwNCIsIi0wLjI1NDg4OTM1MjM5NjgwMDMzIiwiMS4wODcyODc5Mjc2MDMxOTk3Il19XQ--
 *
 * @author fengquanwei
 * @create 2019/4/10 15:36
 **/
public class GrowthUtil {
    private static Logger logger = LoggerFactory.getLogger(GrowthUtil.class);

    /**
     * 获取线性增长值（y = x, 参数值范围：[0,1]）
     */
    public static double getLinearGrowthValue(double x) {
        if (x < 0 || x > 1) {
            logger.error("参数异常, 正常取值范围: [0,1], 当前参数: {}", x);
            return 0;
        }

        return x;
    }

    /**
     * 获取指数增长值（y = x^2, 参数值范围：[0,1]）
     */
    public static double getExponentialGrowthValue(double x) {
        if (x < 0 || x > 1) {
            logger.error("参数异常, 正常取值范围: [0,1], 当前参数: {}", x);
            return 0;
        }

        return Math.pow(x, 2);
    }

    /**
     * 获取对数增长值（y = log(100x)/2, 参数值范围：[0.01,1]）
     */
    public static double getLogarithmicGrowthValue(double x) {
        // 由于 1 的对数是 0，为避免结果值小于 0：限制入参必须大于等于 0.01
        if (x < 0.01 || x > 1) {
            logger.error("参数异常, 正常取值范围: [0.01,1], 当前参数: {}", x);
            return 0;
        }

        return Math.log10(100 * x) / 2;
    }

    /**
     * 获取相对值（返回值范围：[0,1]）
     */
    public static double getRelativeValue(long current, long min, long max) {
        return (double) (current - min) / (max - min);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            double relativeValue = getRelativeValue(i, 0, 100);

            double linearGrowthValue = getLinearGrowthValue(relativeValue);
            logger.info("原值: {}, 相对值: {}, 线性增长值: {}", i, relativeValue, linearGrowthValue);
        }

        for (int i = 0; i <= 100; i++) {
            double relativeValue = getRelativeValue(i, 0, 100);

            double exponentialGrowthValue = getExponentialGrowthValue(relativeValue);
            logger.info("原值: {}, 相对值: {}, 指数增长值: {}", i, relativeValue, exponentialGrowthValue);
        }

        for (int i = 0; i <= 100; i++) {
            double relativeValue = getRelativeValue(i, 0, 100);

            double logarithmicGrowthValue = getLogarithmicGrowthValue(relativeValue);
            logger.info("原值: {}, 相对值: {}, 对数增长值: {}", i, relativeValue, logarithmicGrowthValue);
        }
    }

}
