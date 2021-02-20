package cn.wangoon.constants;

/**
 * @Description Job 用到的常量
 * @PackagePath cn.wangoon.common.constants.JobsConstants
 * @Author YINZHIYU
 * @Date 2020/4/26 13:42
 * @Version 1.0.0.0
 **/
public interface JobsConstants {
    /**
     * Jobs 参数
     */
    String SHARDING_PARAM_TASK_TYPE = "taskType";
    String SHARDING_PARAM_SITE = "site";
    String SHARDING_PARAM_ORDER_STATUS = "orderStatus";


    String SYSTEM_LISTENER_JOB_CLASS_BEAN_NAME = "SystemListenerJob";

    Integer RETRY_COUNT = 2;//重试次数

    /**
     * 默认表达式，默认十分钟跑一次
     */
    String DEFAULT_CRON_EXPRESSION = "0 */10 * * * ?";
}
