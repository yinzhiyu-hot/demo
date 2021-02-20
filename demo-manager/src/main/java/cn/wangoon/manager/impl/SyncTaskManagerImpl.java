package cn.wangoon.manager.impl;

import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.dao.mapper.SyncTaskDataMapper;
import cn.wangoon.dao.mapper.SyncTaskMapper;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.domain.entity.SyncTaskData;
import cn.wangoon.manager.SyncTaskManager;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 异步任务
 * @PackagePath cn.wangoon.manager.impl.SyncTaskManagerImpl
 * @Author YINZHIYU
 * @Date 2020-04-17 16:30:00
 * @Version 1.0.0.0
 **/
@Component
public class SyncTaskManagerImpl implements SyncTaskManager {

    @Resource
    private SyncTaskMapper syncTaskMapper;

    @Resource
    private SyncTaskDataMapper syncTaskDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSyncTask(SyncTask syncTask) {
        boolean result;
        try {
            //插入主档
            syncTask.setId(null);
            result = SqlHelper.retBool(syncTaskMapper.insert(syncTask));

            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl ==> insertSyncTask ==> syncTaskMapper.insert(syncTask) return false");
            }
            //插入明细
            syncTask.getSyncTaskDataList().stream().filter(bean -> {
                bean.setTaskId(syncTask.getId());
                return true;
            }).collect(Collectors.toList());

            result = SqlHelper.retBool(syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()));

            if (!result) {
                throw new RuntimeException("syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) return false");
            }

        } catch (Exception ex) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            LogUtils.error(syncTask, "持久化异步任务异常", ex);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertSyncTask(List<SyncTask> syncTaskList) {
        boolean result = false;
        try {
            for (SyncTask syncTask : syncTaskList) {
                //插入主档
                syncTask.setId(null);
                result = SqlHelper.retBool(syncTaskMapper.insert(syncTask));

                if (!result) {
                    throw new RuntimeException("SyncTaskManagerImpl ==> batchInsertSyncTask ==> syncTaskMapper.insert(syncTask) return false");
                }
                //插入明细
                syncTask.getSyncTaskDataList().stream().filter(bean -> {
                    bean.setTaskId(syncTask.getId());
                    return true;
                }).collect(Collectors.toList());

                result = SqlHelper.retBool(syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()));

                if (!result) {
                    throw new RuntimeException("SyncTaskManagerImpl ==> batchInsertSyncTask ==> syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) return false");
                }
            }
        } catch (Exception ex) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            LogUtils.error(syncTaskList, "批量持久化异步任务异常", ex);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSyncTaskData(SyncTask syncTask) {
        boolean result;
        try {
            QueryWrapper<SyncTaskData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SyncTaskData.COL_TASK_ID, syncTask.getId());
            result = SqlHelper.retBool(syncTaskDataMapper.delete(queryWrapper));
            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl ==> updateSyncTaskData ==> syncTaskDataMapper.delete(queryWrapper) return false");
            }

            //插入明细
            syncTask.getSyncTaskDataList().stream().filter(bean -> {
                bean.setTaskId(syncTask.getId());
                return true;
            }).collect(Collectors.toList());

            result = SqlHelper.retBool(syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()));

            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl ==> updateSyncTaskData ==> syncTaskDataMapper.batchInsert(syncTask.getSyncTaskDataList()) return false");
            }

        } catch (Exception ex) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            LogUtils.error(syncTask, "更新异步任务数据异常", ex);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int dataCarryForward(List<Long> idList) {
        int delteCount = 0;
        try {
            QueryWrapper<SyncTaskData> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(SyncTaskData.COL_TASK_ID, idList);
            boolean result = SqlHelper.retBool(syncTaskDataMapper.delete(queryWrapper));

            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl ==> dataCarryForward ==> syncTaskDataMapper.delete(queryWrapper) return false");
            }
            delteCount = syncTaskMapper.deleteBatchIds(idList);
            result = SqlHelper.retBool(delteCount);
            if (!result) {
                throw new RuntimeException("SyncTaskManagerImpl ==> dataCarryForward ==> syncTaskMapper.deleteBatchIds(idList) return false");
            }

        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            LogUtils.error(idList, "结转任务异常", ex);
        }
        return delteCount;
    }
}
