package cn.wangoon.dao.mapper;

import cn.wangoon.domain.entity.SyncTaskData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 同步任务数据 Mapper 接口
 * </p>
 *
 * @author yinzhiyu
 * @since 2019-10-14
 */
public interface SyncTaskDataMapper extends BaseMapper<SyncTaskData> {

    int batchInsert(@Param("list") List<SyncTaskData> list);
}
