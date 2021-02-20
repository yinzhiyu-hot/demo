package cn.wangoon.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 带占位符的格式化 [也可以使用 java.text.MessageFormat]
 * @Author YINZHIYU
 * @Date 2019-10-09 10:15:00
 * @Version 1.0.0.0
 **/
public class StringFormatter {

    /**
     * @Description 带占位符的格式化
     * @Params ==>
     * @Param value
     * @Param args
     * @Return java.lang.String
     * @Date 2020/4/21 10:04
     * @Auther YINZHIYU
     */
    public static String format(String value, Object[] args) {

        for (int i = 0; i < args.length; i++) {
            Pattern r = Pattern.compile("\\D{" + i + "}\\D");
            Matcher m = r.matcher(value);
            if (m.find()) {
                value = value.replace("{" + i + "}", args[i].toString());
            }
        }
        return value;
    }

    /**
     * @Description 带占位符的格式化
     * @Params ==>
     * @Param args
     * @Return java.lang.String
     * @Date 2020/4/21 10:04
     * @Auther YINZHIYU
     */
    public static String format(Object... args) {
        String value = args[0].toString();
        for (int i = 1; i < args.length; i++) {
            Pattern r = Pattern.compile("\\D{" + (i - 1) + "}\\D");
            Matcher m = r.matcher(value);
            if (m.find()) {
                value = value.replace("{" + (i - 1) + "}", args[i].toString());
            }
        }
        return value;
    }
}
