package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增长工具类
 * http://zuotu.91maths.com/#W3sidHlwZSI6MCwiZXEiOiJ4IiwiY29sb3IiOiIjMDAwMDAwIn0seyJ0eXBlIjowLCJlcSI6InheMiIsImNvbG9yIjoiIzAwMDAwMCJ9LHsidHlwZSI6MCwiZXEiOiJsb2coMTAweCsxKS8yIiwiY29sb3IiOiIjMDAwMDAwIn0seyJ0eXBlIjoxMDAwLCJ3aW5kb3ciOlsiLTAuNzIwODk1OTk5OTk5OTk5NSIsIjIuMDA1NDAxNTk5OTk5OTk5NSIsIi0wLjM1Mjc4MDI4Nzk5OTk5OTkiLCIxLjMyNDk0MTMxMiJdfV0-
 *
 * @author fengquanwei
 * @create 2019/4/10 15:36
 **/
public class GrowthUtil {
    private static Logger logger = LoggerFactory.getLogger(GrowthUtil.class);

    /**
     * 线性增长（y = x, 0 <= x <=1）
     */
    public static double linearGrowth(double x) {
        if (x < 0 || x > 1) {
            return 0;
        }

        return x;
    }

    /**
     * 指数增长（y = x^2, 0 <= x <=1）
     */
    public static double exponentialGrowth(double x) {
        if (x < 0 || x > 1) {
            return 0;
        }

        return Math.pow(x, 2);
    }

    /**
     * 对数增长（y = log(100x + 1)/2, 0 <= x <=1）
     */
    public static double logarithmicGrowth(double x) {
        if (x < 0 || x > 1) {
            return 0;
        }

        return Math.log10(100 * x + 1) / 2;
    }

    public static void main(String[] args) {
        for (double i = 0; i < 1; i += 0.1) {
            logger.info("线性增长：{}", linearGrowth(i));
            logger.info("指数增长：{}", exponentialGrowth(i));
            logger.info("对数增长：{}", logarithmicGrowth(i));
            logger.info("");
        }
    }

}
