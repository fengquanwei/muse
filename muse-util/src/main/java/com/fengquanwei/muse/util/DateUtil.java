package com.fengquanwei.muse.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author fengquanwei
 * @create 2019/1/2 11:52
 **/
public class DateUtil {
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 格式化
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }

        return format(date, null);
    }

    /**
     * 格式化
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        if (format == null || format.length() == 0) {
            format = DATE_FORMAT_YYYYMMDDHHMMSS;
        }

        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date()));
        System.out.println(DateUtil.format(new Date(), DATE_FORMAT_YYYYMMDDHHMMSSSSS));
    }
}
