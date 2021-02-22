package cn.wangoon.job.common.datacarry;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.wangoon.common.annotations.CronExpression;
import cn.wangoon.common.annotations.ShardingItemParams;
import cn.wangoon.common.annotations.ShardingTotalCount;
import cn.wangoon.common.constants.SysBaseConfigConstants;
import cn.wangoon.service.business.base.OmsLogService;
import cn.wangoon.job.BaseSimpleJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 日志表数据结转 Job
 * @Remark
 * @PackagePath cn.wangoon.service.job.datacarry.omslog.LogDataCarryForwardJob
 * @Author YINZHIYU
 * @Date 2020/10/10 20:45
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
    private OmsLogService omsLogService;

    @Override
    public void executeJob(ShardingContext shardingContext) {
        String recordDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), SysBaseConfigConstants.LOG_CARRY_FORWARD_DAY_OFFSET), DatePattern.NORM_DATE_PATTERN);
        omsLogService.dataCarryForward(recordDate);
    }
}