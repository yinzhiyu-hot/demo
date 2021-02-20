package cn.wangoon.job.factory.wms;

import cn.wangoon.common.enums.SyncTaskTypeEnum;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.job.factory.DataSync;
import cn.wangoon.job.sync.wms.biz.impl.WmsDataSyncServiceImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description Wms数据同步路由适配[根据taskType 调用业务逻辑bean不同方法 ==> taskType 对应枚举中method  即方法名]
 * @PackagePath cn.wangoon.service.job.factory.wms.WmsDataSync
 * @Author YINZHIYU
 * @Date 2020-04-14 10:26:00
 * @Version 1.0.0.0
 **/
@Component
public class WmsDataSync extends DataSync {

    @Override
    protected boolean adapter(SyncTask syncTask) throws Exception {
        Method method = WmsDataSyncServiceImpl.class.getMethod(SyncTaskTypeEnum.getMethod(syncTask.getTaskType()), SyncTask.class);
        return (boolean) method.invoke(SpringBootBeanUtil.getBean(WmsDataSyncServiceImpl.class), syncTask);
    }
}
