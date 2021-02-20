package cn.wangoon.common.constants;

/**
 * @Description 基础配置常量
 * @Remark
 * @PackagePath cn.wangoon.common.constants.SysBaseConfigConstants
 * @Author Thomas
 * @Date 2021/2/20 17:34
 * @Version 1.0.0.0
 **/
public interface SysBaseConfigConstants {

    /**
     * 重试次数
     */
    String RETRY_COUNT = "RETRY_COUNT";
    String RETRY_COUNT_SYNC_TASK = "RETRY_COUNT_SYNC_TASK";//同步任务重置次数

    /**
     * 前缀
     */
    String PREFIX = "PREFIX";
    String PREFIX_REMOVE = "PREFIX_REMOVE";//前缀移除

    /**
     * 后缀
     */
    String SUFFIX = "SUFFIX";
    String SUFFIX_REMOVE = "SUFFIX_REMOVE";//后缀移除

    /**
     * 日志结转天数
     */
    int LOG_CARRY_FORWARD_DAY_OFFSET = -15;

    /**
     * 同步任务结转天数
     */
    int TASK_CARRY_FORWARD_DAY_OFFSET = -15;
}
