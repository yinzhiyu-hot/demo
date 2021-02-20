package cn.wangoon.service.business.base.impl;

import cn.wangoon.dao.mapper.SyncTaskMapper;
import cn.wangoon.domain.dto.SyncTaskDto;
import cn.wangoon.domain.entity.SysLog;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.domain.vo.SyncTaskChartVO;
import cn.wangoon.manager.SyncTaskManager;
import cn.wangoon.service.business.base.OmsLogService;
import cn.wangoon.service.business.base.SyncTaskService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 异步任务
 * @PackagePath cn.wangoon.service.business.impl.SyncTaskService
 * @Author YINZHIYU
 * @Date 2020-04-14 18:24:00
 * @Version 1.0.0.0
 **/
@Service
public class SyncTaskServiceImpl extends ServiceImpl<SyncTaskMapper, SyncTask> implements SyncTaskService {

    @Resource
    private SyncTaskManager syncTaskManager;

    @Resource
    private OmsLogService omsLogService;

    @Override
    public boolean insertSyncTask(SyncTask syncTask) {
        return syncTaskManager.insertSyncTask(syncTask);
    }

    @Override
    public boolean batchInsertSyncTask(List<SyncTask> syncTaskList) {
        return syncTaskManager.batchInsertSyncTask(syncTaskList);
    }

    @Override
    public boolean updateSyncTaskData(SyncTask syncTask) {
        return syncTaskManager.updateSyncTaskData(syncTask);
    }

    @Override
    public List<SyncTask> selectList(Wrapper<SyncTask> wrapper) {
        return super.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto) {
        return this.baseMapper.getProcessTaskList(syncTaskDto);
    }

    @Override
    public List<SyncTaskChartVO> getTaskChartsList(SyncTaskDto syncTaskDto) {
        return this.baseMapper.getTaskChartsList(syncTaskDto);
    }

    @Override
    public List<SyncTask> getProcessFailTask(String orderNumber) {
        return this.baseMapper.getProcessFailTask(orderNumber);
    }

    @Override
    public IPage<SyncTask> listSyncTaskByPage(Page<SyncTask> page, QueryWrapper queryWrapper) {
        return this.baseMapper.listSyncTaskByPage(page, queryWrapper);
    }

    @Override
    public List<Long> getDataCarryForwardSyncTaskList(String createDate) {
        return this.baseMapper.getDataCarryForwardSyncTaskList(createDate);
    }

    @Override
    public void dataCarryForward(List<Long> idList) {
        int count = syncTaskManager.dataCarryForward(idList);
        omsLogService.recordLog(new SysLog("dataCarryForward", String.format("本次结转sync_task任务表数据记录 %s 条", count)));
    }
}
