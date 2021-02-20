package cn.wangoon.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description 邮件信息配置
 * @Remark
 * @PackagePath cn.wangoon.common.config.MailConfig
 * @Author YINZHIYU
 * @Date 2021/1/1 11:16
 * @Version 1.0.0.0
 **/
@Component
public class MailConfig {

    /**
     * 抄送人列表
     */
    @Value("${Mail.username.to}")
    public String mailUsernameTo;

    /**
     * 抄送人列表
     */
    @Value("${Mail.username.cc}")
    public String mailUsernameCc;
}
