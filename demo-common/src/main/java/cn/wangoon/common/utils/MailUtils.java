package cn.wangoon.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.wangoon.common.config.MailConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 邮件发送工具类
 * @Remark
 * @PackagePath cn.wangoon.common.utils.MailUtils
 * @Author YINZHIYU
 * @Date 2021/1/1 11:38
 * @Version 1.0.0.0
 **/
@Component
public class MailUtils {
    @Resource
    private MailConfig mailConfig;

    public void send(String content) {
        ThreadUtil.execAsync(() -> {
            try {
                MailUtil.send(mailConfig.mailUsernameTo, mailConfig.mailUsernameCc, StrUtil.EMPTY, String.format("Oms系统自动发送[%s]", DateUtil.date().toString()), String.join("\r\n ==>", NetUtils.getLocalIP(), content), false);
            } catch (Exception ex) {
                LogUtils.error(String.format("Oms系统自动发送邮件异常：%s\r\n发送内容：%s", ex, content));
            }
        });
    }
}
