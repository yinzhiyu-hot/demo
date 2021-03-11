package cn.wangoon.job.sync.wms;

import cn.wangoon.common.annotations.CronExpression;
import cn.wangoon.common.annotations.ShardingItemParams;
import cn.wangoon.common.annotations.ShardingTotalCount;
import cn.wangoon.job.BaseSimpleJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

/**
 * @Description WMS业务数据同步job
 * @PackagePath cn.wangoon.service.job.sync.wms.WmsSyncTaskJob
 * @Author YINZHIYU
 * @Date 2020-04-14 10:07:00
 * @Version 1.0.0.0
 **/
@Component("WmsSyncTaskJob")
@Description("Wms接口调用 同步Job")
@ShardingTotalCount(1)
@ShardingItemParams("0=taskType:ALLOCATION_ORDER_WMS")
@CronExpression("*/10 * * * * ?")
@DisallowConcurrentExecution
public class WmsSyncTaskJob extends BaseSimpleJob {

    @Override
    public void executeJob(ShardingContext shardingContext) {
        super.startExecuteJob(shardingContext);
    }
}