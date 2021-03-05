package cn.wangoon.job.common.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.wangoon.cache.JobsConfigCache;
import cn.wangoon.cache.SysBaseConfigCache;
import cn.wangoon.common.annotations.CronExpression;
import cn.wangoon.common.annotations.ShardingItemParams;
import cn.wangoon.common.annotations.ShardingTotalCount;
import cn.wangoon.common.config.NetConfig;
import cn.wangoon.common.constants.RedisConstants;
import cn.wangoon.common.enums.JobStatusEnum;
import cn.wangoon.common.utils.CastUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.RedisUtils;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.config.JobsConfig;
import cn.wangoon.domain.entity.SysBaseConfig;
import cn.wangoon.domain.entity.SysJobConfig;
import cn.wangoon.service.business.base.SysJobConfigService;
import cn.wangoon.job.BaseSimpleJob;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description 系统监听JOB
 * @PackagePath cn.wangoon.service.job.listener.SystemListenerJob
 * @Author YINZHIYU
 * @Date 2020-04-20 13:48:00
 * @Version 1.0.0.0
 **/
@Component("SystemListenerJob")
@Description("系统 监听Job")
@ShardingTotalCount(1)
@ShardingItemParams("")
@CronExpression("*/5 * * * * ?")
@DisallowConcurrentExecution
public class SystemListenerJob extends BaseSimpleJob {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysBaseConfigCache sysBaseConfigCache;

    @Resource
    private SysJobConfigService sysJobConfigService;

    @Resource
    private JobsConfig jobsConfig;

    @Resource
    private NetConfig netConfig;

    @Override
    protected void executeJob(ShardingContext shardingContext) {

        //同步job配置
        syncSysJobConfigCache();

        //同步基础配置
        syncSysBaseConfigCache();
    }

    /*
     * @Description 同步JOB配置 [多实例部署时，监控其它实例是否有更新配置，有则同步]
     * @Remark 此处适用双实例，超过2个则建议采用MQ消息广发处理
     * @Params ==>
     * @Return void
     * @Date 2021/2/22 15:28
     * @Auther Thomas
     */
    private void syncSysJobConfigCache() {
        try {
            ConcurrentMap<String, SysJobConfig> sysJobConfigMapLocal = JobsConfigCache.sysJobConfigMap;//取本地 JOB 调度配置

            //获取Redis 中 SysJobConfig，管理jobScheduler的启停
            ConcurrentMap<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

            if (ObjectUtil.isEmpty(sysJobConfigMapRedis)) {
                return;
            }

            for (Map.Entry<String, SysJobConfig> entry : sysJobConfigMapRedis.entrySet()) {

                //找不到
                if (ObjectUtil.isEmpty(sysJobConfigMapLocal.get(entry.getKey()))) {
                    continue;
                }
                //如果本地状态和Redis存放的状态不一致，说明有人启停了job，并更新了Redis的jobs配置
                if (!Objects.equals(entry.getValue().getJobStatus(), sysJobConfigMapLocal.get(entry.getKey()).getJobStatus())) {

                    //管理job启停
                    JobsConfigCache.jobSchedulerMap.get(entry.getKey()).getSchedulerFacade().registerStartUpInfo(JobStatusEnum.getStart(entry.getValue().getJobStatus()));

                    //更新数据库job 配置状态
                    SysJobConfig sysJobConfigLocal = entry.getValue();
                    sysJobConfigService.updateByCondition(sysJobConfigLocal);

                    //同步更新覆盖本地Job配置map
                    JobsConfigCache.sysJobConfigMap.put(entry.getKey(), entry.getValue());

                    LogUtils.info(String.format("心跳状态监测Job ==> Job:%s ==> 执行 %s", entry.getKey(), JobStatusEnum.getRemark(entry.getValue().getJobStatus())));
                }

                //如果是更新配置操作
                if (entry.getValue().isUpdateFlag() && !StrUtil.equals(netConfig.getLocalIpPort(), entry.getValue().getUpdateIpPort())) {
                    //更新调度配置信息
                    ElasticJob elasticJob = CastUtil.cast(SpringBootBeanUtil.getBean(entry.getKey()));
                    LiteJobConfiguration liteJobConfiguration = jobsConfig.getLiteJobConfiguration(elasticJob, entry.getValue().getCronExpression(), entry.getValue().getShardingTotalCount(), entry.getValue().getShardingItemParams());
                    JobsConfigCache.jobSchedulerMap.get(entry.getKey()).getSchedulerFacade().updateJobConfiguration(liteJobConfiguration);

                    //更新数据库job配置信息
                    SysJobConfig sysJobConfigLocal = entry.getValue();
                    sysJobConfigService.updateByCondition(sysJobConfigLocal);

                    //同步更新覆盖本地Job配置map
                    JobsConfigCache.sysJobConfigMap.put(entry.getKey(), entry.getValue());

                    //更新Redis 的更新标识
                    entry.getValue().setUpdateFlag(false);
                    sysJobConfigMapRedis.put(entry.getKey(), entry.getValue());
                    try {
                        redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);
                    } catch (Exception e) {
                        LogUtils.error(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis, "redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis) ==> 处理异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error("处理异常", e);
        }
    }

    /*
     * @Description 同步基础配置 [多实例部署时，监控其它实例是否有更新配置，有则同步]
     * @Remark 此处适用双实例部署，超过2个则建议采用MQ消息广发处理
     * @Params ==>
     * @Return void
     * @Date 2021/2/22 15:28
     * @Auther Thomas
     */
    private void syncSysBaseConfigCache() {
        try {
            //获取Redis 中 sysBaseConfig 存储的操作[更新/删除/新增]信息
            SysBaseConfig sysBaseConfig = null;//取Redis 基础配置

            try {
                sysBaseConfig = redisUtils.getValue(RedisConstants.SYS_BASE_CONFIG_MAP_KEY);
            } catch (Exception e) {
                LogUtils.error(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, "redisUtils.getValue(RedisConstants.SYS_BASE_CONFIG_MAP_KEY) ==> 处理异常", e);
            }

            if (ObjectUtil.isEmpty(sysBaseConfig)) {
                return;
            }

            //如果存在更新/删除/新增配置操作
            if (sysBaseConfig.isUpdateFlag() && !StrUtil.equals(netConfig.getLocalIpPort(), sysBaseConfig.getUpdateIpPort())) {

                //重新从数据库加载基础配置
                sysBaseConfigCache.init();

                //更新Redis 的更新标识
                sysBaseConfig.setUpdateFlag(false);
                try {
                    redisUtils.set(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig);
                } catch (Exception e) {
                    LogUtils.error(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig, "redisUtils.set(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig) ==> 处理异常", e);
                }
            }
        } catch (Exception e) {
            LogUtils.error("处理异常", e);
        }
    }
}
