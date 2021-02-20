package cn.wangoon.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * @Description 订单工具
 * @PackagePath cn.wangoon.common.utils.OrderUtils
 * @Author YINZHIYU
 * @Date 2020/5/20 9:31
 * @Version 1.0.0.0
 **/
public class OrderUtils {

    /**
     * @Description 时间戳
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/20 10:54
     * @Auther YINZHIYU
     */
    public static String genralNumber() {
        ThreadUtil.sleep(10);
        return String.valueOf(DateUtil.date().getTime());
    }

    /**
     * @Description 前缀+时间戳+5位随机数
     * @Params ==>
     * @Param prefix
     * @Return java.lang.String
     * @Date 2020/5/20 10:55
     * @Auther YINZHIYU
     */
    public static String genralNumber(String prefix) {
        ThreadUtil.sleep(10);
        StringBuilder number = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            number.append(prefix).append("-");
        }
        number.append(DateUtil.date().getTime());
        number.append(RandomUtil.randomNumbers(5));
        return number.toString();
    }

    /**
     * @Description 前缀+时间格式化规则+5位随机数
     * @Params ==>
     * @Param prefix
     * @Param format 时间格式化规则(yyyyMMdd等)
     * @Return java.lang.String
     * @Date 2020/5/20 10:55
     * @Auther YINZHIYU
     */
    public static String genralNumber(String prefix, String format) {
        ThreadUtil.sleep(10);
        StringBuilder number = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            number.append(prefix).append("-");
        }
        number.append(DateUtil.format(DateUtil.date(), format));
        number.append(RandomUtil.randomNumbers(5));
        return number.toString();
    }

    /**
     * @Description 前缀+连接字符+时间格式化规则+5位随机数
     * @Remark
     * @Params ==>
     * @Param prefix 前缀
     * @Param format 时间格式化规则(yyyyMMdd等)
     * @Param joinCharSequence 连接字符
     * @Return java.lang.String
     * @Date 2021/1/15 16:42
     * @Auther YINZHIYU
     */
    public static String genralNumber(String prefix, String format, String joinCharSequence) {
        ThreadUtil.sleep(10);
        StringBuilder number = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            number.append(prefix);
        }
        if (ObjectUtil.isNotEmpty(joinCharSequence)) {
            number.append(joinCharSequence);
        }
        number.append(DateUtil.format(DateUtil.date(), format));
        number.append(RandomUtil.randomNumbers(5));
        return number.toString();
    }
}
