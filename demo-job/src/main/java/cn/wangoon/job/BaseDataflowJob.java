package cn.wangoon.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.domain.entity.SysJobConfig;
import cn.wangoon.domain.query.SysJobConfigQuery;
import cn.wangoon.service.business.base.SysJobConfigService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Description 流式处理Job
 * @Remark oms统一采用非流式处理数据规则[streamingProcess: false 是否流式处理数据]
 * 可通过DataflowJobConfiguration配置是否流式处理。
 * 1.流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去；
 * 2.非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
 * 如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。
 * 流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
 * @PackagePath cn.wangoon.service.job.BaseDataflowJob
 * @Author YINZHIYU
 * @Date 2020/10/22 14:18
 * @Version 1.0.0.0
 **/
@Component
public abstract class BaseDataflowJob<T> implements DataflowJob<T> {

    @Resource
    private SysJobConfigService sysJobConfigService;

    /**
     * 获取待处理数据.
     *
     * @param shardingContext 分片上下文
     * @return 待处理的数据集合
     */
    protected abstract List<T> startFetchData(ShardingContext shardingContext);

    /**
     * 处理数据.
     *
     * @param shardingContext 分片上下文
     * @param data            待处理数据集合
     */
    protected abstract void startProcessData(ShardingContext shardingContext, List<T> data);

    @Override
    public List<T> fetchData(ShardingContext shardingContext) {
        List<T> dataListResult = Lists.newArrayList();
        String radomStr = RandomUtil.randomString(6);
        String jobName = shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1);
        try {
            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 开始数据获取工作，任务总片数: %s, 当前分片项: %s, 当前参数: %s, 当前任务名称: %s",
                    radomStr,
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1)
            ));
            //心跳记录
            heatRecord(jobName, radomStr);

            // 程序开始时间
            Instant startTime = Instant.now();

            // 执行数据获取工作
            List<T> dataList = startFetchData(shardingContext);

            if (ObjectUtil.isNotEmpty(dataList)) {
                dataListResult.addAll(dataList);
            }

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 结束数据获取工作，执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), millis));

        } catch (Exception e) {
            LogUtils.error(shardingContext, String.format("[%s]Thread ID: %s, Job 数据获取工作异常", radomStr, Thread.currentThread().getId()), e);
        }
        return dataListResult;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<T> data) {
        String radomStr = RandomUtil.randomString(6);
        String jobName = shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1);
        try {
            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 开始数据处理工作，任务总片数: %s, 当前分片项: %s, 当前参数: %s, 当前任务名称: %s",
                    radomStr,
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName().substring(shardingContext.getJobName().lastIndexOf(".") + 1)
            ));
            //心跳记录
            heatRecord(jobName, radomStr);

            // 程序开始时间
            Instant startTime = Instant.now();

            // 执行工作
            startProcessData(shardingContext, data);

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(shardingContext, String.format("[%s]Thread ID: %s, Job 结束数据处理工作，执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), millis));

        } catch (Exception e) {
            LogUtils.error(shardingContext, String.format("[%s]Thread ID: %s, Job 数据处理工作异常", radomStr, Thread.currentThread().getId()), e);
        }
    }

    /*
     * @Description 心跳记录[Job执行一次后记录一次心跳]
     * 备注：此处记录可以放到作业监听中去做，创建监听实现ElasticJobListener，在创建调度的时候加入监听即可，我比较懒，就不自己新写监听了
     * @Params ==>
     * @Param jobName
     * @Return void
     * @Date 2020/6/16 11:15
     * @Auther YINZHIYU
     */
    protected void heatRecord(String jobName, String radomStr) {

        boolean recordResult = false;
        try {
            SysJobConfigQuery sysJobConfigQuery = new SysJobConfigQuery();
            sysJobConfigQuery.setJobName(jobName);
            SysJobConfig sysJobConfig = sysJobConfigService.getSysJobConfigByCondition(sysJobConfigQuery);

            if (ObjectUtil.isNotEmpty(sysJobConfig)) {
                SysJobConfig sysJobConfigHeartRecord = new SysJobConfig();
                sysJobConfigHeartRecord.setId(sysJobConfig.getId());
                sysJobConfigHeartRecord.setUpdateTime(DateUtil.date());
                sysJobConfigHeartRecord.setUpdateUser(jobName);
                recordResult = sysJobConfigService.updateById(sysJobConfigHeartRecord);
            }
        } catch (Exception e) {
            LogUtils.error(jobName, radomStr, String.format("[%s]Thread ID: 心跳记录异常", Thread.currentThread().getId()), e);
        }
        LogUtils.info(jobName, radomStr, String.format("[%s]Thread ID: %s, %s 心跳记录 %s。", radomStr, Thread.currentThread().getId(), jobName, recordResult ? "成功" : "失败"));
    }
}
