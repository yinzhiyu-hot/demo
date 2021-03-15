package cn.wangoon.config;

import cn.wangoon.cache.SysBaseConfigCache;
import cn.wangoon.common.utils.LogUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Description 系统启动中加载各种全局配置
 * @PackagePath cn.wangoon.config.JobsConfigCache
 * @Author YINZHIYU
 * @Date 2020-04-13 09:55:00
 * @Version 1.0.0.0
 **/
@Component
public class AppCacheConfig {

    @Resource
    public SysBaseConfigCache sysBaseConfigCache;

    @PostConstruct
    public void init() {
        LogUtils.info("系统启动中");

        //初始化基础配置
        sysBaseConfigCache.init();
    }

    @PreDestroy
    public void destroy() {
        LogUtils.info("系统运行结束");
    }
}
