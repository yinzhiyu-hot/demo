package cn.wangoon.manager;

import cn.wangoon.domain.entity.SyncTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 异步任务 Mapper 接口
 * </p>
 *
 * @author yinzhiyu
 * @since 2019-10-14
 */
@Component
public interface SyncTaskManager {

    boolean insertSyncTask(SyncTask syncTask);

    boolean batchInsertSyncTask(List<SyncTask> syncTaskList);

    boolean updateSyncTaskData(SyncTask syncTask);

    int dataCarryForward(List<Long> idList);
}
