package cn.wangoon.job.common.datacarry;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.wangoon.common.annotations.CronExpression;
import cn.wangoon.common.annotations.ShardingItemParams;
import cn.wangoon.common.annotations.ShardingTotalCount;
import cn.wangoon.common.constants.SysBaseConfigConstants;
import cn.wangoon.service.business.base.SyncTaskService;
import cn.wangoon.job.BaseDataflowJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 任务表数据结转 Job
 * @Remark
 * @PackagePath cn.wangoon.service.job.datacarry.synctask.SyncTaskDataCarryDataForwardFlowJob
 * @Author YINZHIYU
 * @Date 2020/10/26 11:35
 * @Version 1.0.0.0
 **/
@Component("SyncTaskDataCarryDataForwardFlowJob")
@Description("任务表数据结转 Job")
@ShardingTotalCount(1)
@ShardingItemParams("")
@CronExpression("0 0 4 * * ?")
@DisallowConcurrentExecution
public class SyncTaskDataCarryDataForwardFlowJob extends BaseDataflowJob<Long> {

    @Resource
    private SyncTaskService syncTaskService;

    @Override
    protected List<Long> startFetchData(ShardingContext shardingContext) {
        String createDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), SysBaseConfigConstants.TASK_CARRY_FORWARD_DAY_OFFSET), DatePattern.NORM_DATE_PATTERN);
        return syncTaskService.getDataCarryForwardSyncTaskList(createDate);
    }

    @Override
    protected void startProcessData(ShardingContext shardingContext, List<Long> data) {
        syncTaskService.dataCarryForward(data);
    }
}