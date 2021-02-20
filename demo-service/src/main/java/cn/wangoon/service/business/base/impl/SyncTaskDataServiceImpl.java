package cn.wangoon.service.business.base.impl;


import cn.wangoon.dao.mapper.SyncTaskDataMapper;
import cn.wangoon.domain.entity.SyncTaskData;
import cn.wangoon.service.business.base.SyncTaskDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description 异步同步任务数据
 * @PackagePath cn.wangoon.service.business.impl.SyncTaskDataServiceImpl
 * @Author YINZHIYU
 * @Date 2020-04-14 18:24:00
 * @Version 1.0.0.0
 **/
@Service
public class SyncTaskDataServiceImpl extends ServiceImpl<SyncTaskDataMapper, SyncTaskData> implements SyncTaskDataService {

}
