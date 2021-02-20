package cn.wangoon.common.constants;

/**
 * @Description Redis 用到的常量
 * @PackagePath cn.wangoon.common.constants.RedisConstants
 * @Author YINZHIYU
 * @Date 2020-04-20 15:03:00
 * @Version 1.0.0.0
 **/
public interface RedisConstants {
    /**
     * 按天日志
     */
    String LOGS_FORMAT = "yyyy-MM-dd";

    /**
     * 前缀
     */
    String SYS_PREFIX = "OMS:";
 
    /**
     * 配置信息的缓存
     */
    String CONFIG_CACHE = "CONFIG_CACHE:";

    /**
     * Job 调度配置map key
     */
    String SYS_JOB_CONFIG_MAP_KEY = SYS_PREFIX + CONFIG_CACHE + "SYS_JOB_CONFIG_MAP";

    /**
     * 基础配置map key
     */
    String SYS_BASE_CONFIG_MAP_KEY = SYS_PREFIX + CONFIG_CACHE + "SYS_BASE_CONFIG_MAP";

}
