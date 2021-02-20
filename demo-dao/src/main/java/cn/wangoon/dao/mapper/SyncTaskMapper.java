package cn.wangoon.dao.mapper;

import cn.wangoon.domain.dto.SyncTaskDto;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.domain.vo.SyncTaskChartVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 异步任务 Mapper 接口
 * </p>
 *
 * @author yinzhiyu
 * @since 2019-10-14
 */
public interface SyncTaskMapper extends BaseMapper<SyncTask> {

    List<SyncTask> getProcessTaskList(SyncTaskDto syncTaskDto);

    List<SyncTaskChartVO> getTaskChartsList(SyncTaskDto syncTaskDto);

    List<SyncTask> getProcessFailTask(String orderNumber);

    IPage<SyncTask> listSyncTaskByPage(Page<SyncTask> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    List<Long> getDataCarryForwardSyncTaskList(String createDate);

    int batchInsert(@Param("list") List<SyncTask> list);
}
