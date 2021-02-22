package cn.wangoon.common.config;

import cn.wangoon.common.constants.BaseConstants;
import cn.wangoon.common.utils.NetUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @Description 本地Net配置
 * @Remark
 * @PackagePath cn.wangoon.common.config.JobsConfig
 * @Author YINZHIYU
 * @Date 2019-10-15 10:15:00
 * @Version 1.0.0.0
 **/
@Configuration
public class NetConfig {

    @Resource
    Environment environment;

    /**
     * @Description 获取Ip+端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:13
     * @Auther YINZHIYU
     */
    public String getLocalIpPort() {
        return String.format("%s:%s", NetUtils.getLocalIP(), getLocalPort());
    }

    /**
     * @Description 获取应用端口
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/5/22 10:11
     * @Auther YINZHIYU
     */
    public String getLocalPort() {
        return environment.getProperty(BaseConstants.LOCAL_SERVER_PORT);
    }
}