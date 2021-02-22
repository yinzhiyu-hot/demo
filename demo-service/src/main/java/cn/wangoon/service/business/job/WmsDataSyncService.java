package cn.wangoon.service.business.job;

import cn.wangoon.domain.entity.SyncTask;
import org.springframework.stereotype.Component;

/**
 * @Description 数据同步业务处理接口bean
 * @PackagePath cn.wangoon.service.job.sync.wms.biz.WmsDataSyncService
 * @Author YINZHIYU
 * @Date 2020-04-14 10:36:00
 * @Version 1.0.0.0
 **/
@Component
public interface WmsDataSyncService {
    boolean allocationOrderWms(SyncTask syncTask);
}
