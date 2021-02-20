package cn.wangoon.domain.dto;

import cn.wangoon.domain.entity.SyncTask;
import cn.wangoon.domain.entity.SyncTaskData;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @Description 异步任务
 * @PackagePath com.wangoon.domain.dto.SyncTask
 * @Author YINZHIYU
 * @Date 2019-10-14 13:03:00
 * @Version 1.0.0.0
 **/
@Data
public class SyncTaskDto extends SyncTask {

    private int mol;

    private int molValue;

    private String taskData;

    private Integer retryCount;

    private List<SyncTaskData> syncTaskDataList = Lists.newArrayList();
}
