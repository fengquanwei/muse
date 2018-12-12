package com.fengquanwei.muse.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author fengquanwei
 * @create 2018/11/27 17:31
 **/
public class StringUtil {
    /**
     * 计算字符长度
     */
    public static double calculateCharacterLength(String content) {
        double length = 0;

        if (content == null || content.length() == 0) {
            return length;
        }

        // 英文、数字、半角符号、中文分号（\uFF1b）：长度计 0.5
        Pattern pattern1 = Pattern.compile("[A-Za-z0-9\u0000-\u00FF\uFF1b]");
        Matcher matcher1 = pattern1.matcher(content);
        while (matcher1.find()) {
            length += 0.5;
        }

        // 中文、日文、全角符号（不包括中文分号）：长度计 1
        Pattern pattern2 = Pattern.compile("[\u4E00-\u9FA5\uF900-\uFA2D\uFF00-\uFF1a\uFF1c-\uFFFF\u3002\u3001\u201c\u201d\u2018\u2019\u300a\u300b\u3008\u3009\u3010\u3011\u300e\u300F\u300c\u300d\uFe43\uFe44\u3014\u3015\u2026\u2014\uFe4F]");
        Matcher matcher2 = pattern2.matcher(content);
        while (matcher2.find()) {
            length += 1;
        }

        return length;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(calculateCharacterLength("hello world"));
    }
}
