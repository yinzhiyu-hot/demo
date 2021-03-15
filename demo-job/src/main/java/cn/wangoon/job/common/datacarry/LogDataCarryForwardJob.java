package cn.wangoon.job.common.datacarry;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.wangoon.common.annotations.CronExpression;
import cn.wangoon.common.annotations.ShardingItemParams;
import cn.wangoon.common.annotations.ShardingTotalCount;
import cn.wangoon.common.constants.SysBaseConfigConstants;
import cn.wangoon.job.BaseSimpleJob;
import cn.wangoon.service.business.base.SysLogService;
import com.dangdang.ddframe.job.api.ShardingContext;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 日志表数据结转 Job
 * @Remark
 * @PackagePath cn.wangoon.job.common.datacarry.LogDataCarryForwardJob
 * @Author YINZHIYU
 * @Date 2021/3/15 14:51
 * @Version 1.0.0.0
 **/
@Component("LogDataCarryForwardJob")
@Description("日志表数据结转 Job")
@ShardingTotalCount(1)
@ShardingItemParams("")
@CronExpression("0 0 3 * * ?")
@DisallowConcurrentExecution
public class LogDataCarryForwardJob extends BaseSimpleJob {

    @Resource
    private SysLogService sysLogService;

    @Override
    public void executeJob(ShardingContext shardingContext) {
        String recordDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), SysBaseConfigConstants.LOG_CARRY_FORWARD_DAY_OFFSET), DatePattern.NORM_DATE_PATTERN);
        sysLogService.dataCarryForward(recordDate);
    }
}