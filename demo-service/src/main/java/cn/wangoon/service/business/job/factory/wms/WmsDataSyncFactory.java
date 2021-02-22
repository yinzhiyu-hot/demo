package cn.wangoon.service.business.job.factory.wms;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.service.business.job.factory.DataSync;
import cn.wangoon.service.business.job.factory.DataSyncFactory;
import org.springframework.stereotype.Component;

/**
 * @Description Wms 异步任务处理工厂
 * @PackagePath cn.wangoon.service.job.factory.wms.WmsDataSyncFactory
 * @Author YINZHIYU
 * @Date 2020-04-14 11:16:00
 * @Version 1.0.0.0
 **/
@Component
public class WmsDataSyncFactory implements DataSyncFactory {

    private DataSync dataSync;

    @Override
    public DataSync createDataSync() {
        if (ObjectUtil.isEmpty(dataSync)) {
            dataSync = SpringBootBeanUtil.getBean(WmsDataSync.class);
        }
        return dataSync;
    }
}
