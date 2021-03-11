package cn.wangoon.job.adapter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.wangoon.common.utils.CastUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.service.enums.SyncTaskTypeEnum;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @Description 数据同步适配器
 * @Remark
 * @PackagePath cn.wangoon.service.job.adapter.DataSyncAdapter
 * @Author YINZHIYU
 * @Date 2021/3/10 16:22
 * @Version 1.0.0.0
 **/
@Service
public class DataSyncAdapter {

    /**
     * 数据同步执行方法
     *
     * @param syncTask
     * @return
     */
    public boolean excute(SyncTask syncTask) {

        boolean excuteResult = false;

        String radomStr = RandomUtil.randomString(6);

        try {

            // 程序开始时间
            Instant startTime = Instant.now();

            SyncTaskTypeEnum syncTaskTypeEnum = SyncTaskTypeEnum.getSyncTaskTypeEnum(syncTask.getTaskType());

            if (ObjectUtil.isEmpty(syncTaskTypeEnum)) {
                throw new RuntimeException(String.format("无此业务类型[%s]", syncTask.getTaskType()));
            }

            excuteResult = adapter(syncTaskTypeEnum, syncTask);

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(String.format("==> [%s]Thread ID: %s, DataSyncAdapter 数据同步结束, 同步 %s, 执行耗时 %s ms。", radomStr, Thread.currentThread().getId(), excuteResult ? "成功" : "失败", millis));
        } catch (Exception e) {
            throw new RuntimeException(String.format("[%s]Thread ID: %s, DataSyncAdapter 数据同步异常 ==> %s", radomStr, Thread.currentThread().getId(), e));
        }
        return excuteResult;
    }

    protected boolean adapter(SyncTaskTypeEnum syncTaskTypeEnum, SyncTask syncTask) throws Exception {
        Method method = syncTaskTypeEnum.getClazz().getMethod(syncTaskTypeEnum.getMethod(), SyncTask.class);
        return CastUtil.cast(method.invoke(SpringBootBeanUtil.getBean(syncTaskTypeEnum.getClazz()), syncTask));
    }
}
