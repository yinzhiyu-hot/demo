package cn.wangoon.service.business.base;

import cn.wangoon.domain.dto.SyncTaskDto;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.domain.vo.SyncTaskChartVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 异步同步任务
 * @PackagePath cn.wangoon.service.business.SyncTaskService
 * @Author YINZHIYU
 * @Date 2020-04-14 18:24:00
 * @Version 1.0.0.0
 **/
@Component
public interface SyncTaskService extends IService<SyncTask> {

    boolean insertSyncTask(SyncTask syncTask);

    boolean batchInsertSyncTask(List<SyncTask> syncTaskList);

    boolean updateSyncTaskData(SyncTask syncTask);

    List<SyncTask> selectList(Wrapper<SyncTask> wrapper);

    List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto);

    List<SyncTaskChartVO> getTaskChartsList(SyncTaskDto syncTaskDto);

    List<SyncTask> getProcessFailTask(String orderNumber);

    IPage<SyncTask> listSyncTaskByPage(Page<SyncTask> page, QueryWrapper queryWrapper);

    List<Long> getDataCarryForwardSyncTaskList(String createDate);

    void dataCarryForward(List<Long> idList);
}
