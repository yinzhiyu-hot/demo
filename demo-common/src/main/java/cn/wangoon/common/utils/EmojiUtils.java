package cn.wangoon.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.constants.EncodeConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description Emoji 工具类
 * @Remark
 * @PackagePath cn.wangoon.common.utils.EmojiUtils
 * @Author YINZHIYU
 * @Date 2020/10/22 17:10
 * @Version 1.0.0.0
 **/
public class EmojiUtils {
    /**
     * @Description emoji表情替换
     * @Remark
     * @Params ==>
     * @Param source 原字符串
     * @Param slipStr emoji表情替换成的字符串
     * @Return java.lang.String 过滤后的字符串
     * @Date 2020/10/22 17:09
     * @Auther YINZHIYU
     */
    public static String filterEmoji(String source, String slipStr) {
        if (ObjectUtil.isNotEmpty(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        } else {
            return source;
        }
    }

    /**
     * @Description 待转换字符串
     * @Remark 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
     * @Params ==>
     * @Param str
     * @Return java.lang.String 转换后字符串
     * @Date 2020/10/22 17:10
     * @Auther YINZHIYU
     */
    public static String emojiConvert(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), EncodeConstants.ENCODE_UTF8) + "]]");
            } catch (Exception e) {
                return str;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
     * @Remark
     * @Params ==>
     * @Param str 转换后的字符串
     * @Return java.lang.String 转换前的字符串
     * @Date 2020/10/22 17:11
     * @Auther YINZHIYU
     */
    public static String emojiRecovery(String str) {
        String patternString = "\\[\\[(.*?)]]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), EncodeConstants.ENCODE_UTF8));
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}