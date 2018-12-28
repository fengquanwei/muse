package com.fengquanwei.muse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author fengquanwei
 * @create 2018/12/28 19:37
 **/
public class LoggerUtil {
    private static Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void main(String[] args) {
        logger.error("error");
        logger.warn("warn");
        logger.info("info");
        logger.debug("debug");
        logger.trace("trace");
    }
}
