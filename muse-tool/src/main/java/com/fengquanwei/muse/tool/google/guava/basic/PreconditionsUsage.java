package com.fengquanwei.muse.tool.google.guava.basic;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Preconditions 用法
 *
 * @author fengquanwei
 * @create 2018/11/28 10:18
 **/
public class PreconditionsUsage {
    private static Logger logger = LoggerFactory.getLogger(PreconditionsUsage.class);

    public static void main(String[] args) {
        int i = -1;
        try {
            Preconditions.checkArgument(i >= 0, "Argument was %s but expected non negative", i);
        } catch (Exception e) {
            logger.error("checkArgument error", e);
        }

        Integer num = null;
        try {
            System.out.println(Preconditions.checkNotNull(num));
        } catch (Exception e) {
            logger.error("checkNotNull error", e);
        }
    }
}
