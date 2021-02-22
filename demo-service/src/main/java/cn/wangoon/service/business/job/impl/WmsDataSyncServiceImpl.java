package cn.wangoon.service.business.job.impl;

import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.service.business.job.WmsDataSyncService;
import org.springframework.stereotype.Component;

/**
 * @Description Wms 数据同步业务处理实现bean
 * @PackagePath cn.wangoon.service.job.sync.wms.biz.impl.WmsDataSyncServiceImpl
 * @Author YINZHIYU
 * @Date 2020-04-14 10:36:00
 * @Version 1.0.0.0
 **/
@Component
public class WmsDataSyncServiceImpl implements WmsDataSyncService {
    //region 定义

    //endregion

    //region 分配订单到wms
    /*
     * @Description 订单分配Wms
     * @Params ==>
     * @Param syncTask
     * @Return boolean
     * @Date 2020/4/20 16:41
     * @Auther YINZHIYU
     */
    @Override
    public boolean allocationOrderWms(SyncTask syncTask) {
        return true;
    }
    //endregion

}
