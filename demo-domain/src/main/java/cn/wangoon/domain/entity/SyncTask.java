package cn.wangoon.domain.entity;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 异步任务
 * </p>
 *
 * @author yinzhiyu
 * @since 2019-10-14
 */
@Data
@TableName("sync_task")
public class SyncTask implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @TableField("id")
    private Integer id;

    /**
     * 任务类型
     */
    @TableField("task_type")
    private String taskType;

    /**
     * 任务描述
     */
    @TableField("task_desc")
    private String taskDesc;

    /**
     * 任务状态(0 待处理 1 处理中 2 已处理 3 处理失败 99 处理异常 100 终止)
     */
    @TableField("task_status")
    private Integer taskStatus;

    /**
     * 任务执行次数
     */
    @TableField("process_count")
    private Integer processCount;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 完成时间
     */
    @TableField("finish_date")
    private Date finishDate;

    /**
     * 时间戳
     */
    @TableField("ts")
    private Date ts;

    /**
     * 原始数据
     */
    @TableField(exist = false)
    private String taskData;

    /**
     *
     */
    @TableField(exist = false)
    private List<SyncTaskData> syncTaskDataList;

    public String getTaskData() {
        StringBuffer taskDataStr = new StringBuffer();
        if (ObjectUtil.isNotEmpty(syncTaskDataList)) {
            List<SyncTaskData> list = syncTaskDataList.stream().sorted(Comparator.comparing(SyncTaskData::getDataIndex)).collect(Collectors.toList());
            list.forEach(item -> taskDataStr.append(item.getTaskData()));
        }
        if (ObjectUtil.isNotEmpty(taskDataStr.toString())) {
//            this.taskData = StringUtils.base64Decode(taskDataStr.toString());
            this.taskData = taskDataStr.toString();
        }
        return taskData;
    }

    public static final String COL_ID = "id";

    public static final String COL_TASK_TYPE = "task_type";

    public static final String COL_TASK_DESC = "task_desc";

    public static final String COL_TASK_STATUS = "task_status";

    public static final String COL_PROCESS_COUNT = "process_count";

    public static final String COL_CREATE_DATE = "create_date";

    public static final String COL_FINISH_DATE = "finish_date";

    public static final String COL_TS = "ts";
}
