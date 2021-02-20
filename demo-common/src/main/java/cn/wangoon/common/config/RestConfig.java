package cn.wangoon.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description Rest 配置
 * @PackagePath com.example.common.config.RestConfig
 * @Author YINZHIYU
 * @Date 2020-04-09 12:32:00
 * @Version 1.0.0.0
 **/
@Component
public class RestConfig {

    @Value("${rest.dingding.domain}")
    public String dingdingRestUrl;
}
