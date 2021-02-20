package cn.wangoon.domain.entity;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.enums.JobStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @Description 调度任务配置
 * @PackagePath cn.wangoon.SysJobConfig
 * @Author YINZHIYU
 * @Date 2020-04-13 11:04:00
 * @Version 1.0.0.0
 **/
@Data
@TableName(value = "sys_job_config")
public class SysJobConfig {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * job名称
     */
    @TableField(value = "job_name")
    private String jobName;
    /**
     * bean名称
     */
    @TableField(value = "job_class_bean_name")
    private String jobClassBeanName;
    /**
     * 分片数
     */
    @TableField(value = "sharding_total_count")
    private Integer shardingTotalCount;
    /**
     * 分配参数
     */
    @TableField(value = "sharding_item_params")
    private String shardingItemParams;
    /**
     * cron表达式
     */
    @TableField(value = "cron_expression")
    private String cronExpression;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 状态(1 启动 0 停止)
     */
    @TableField(value = "job_status")
    private Integer jobStatus;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String createUser;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 更新标识
     */
    @TableField(exist = false)
    private boolean updateFlag;
    /**
     * 更新IP
     */
    @TableField(exist = false)
    private String updateIpPort;

    /**
     * 存活标记 0 不存活 1 存活
     */
    @TableField(exist = false)
    private Integer lifeFlag;

    /*
     * @Description 默认6小时存活阈值
     * @Params ==>
     * @Param null
     * @Return
     * @Date 2020/6/16 13:00
     * @Auther YINZHIYU
     */
    public Integer getLifeFlag() {
        if (ObjectUtil.isNotEmpty(this.updateTime)) {
            return Objects.equals(this.jobStatus, JobStatusEnum.START.getStatus()) && DateUtil.between(this.updateTime, DateUtil.date(), DateUnit.HOUR) <= 6 ? 1 : 0;
        }
        return 1;
    }

    public static final String COL_ID = "id";

    public static final String COL_JOB_NAME = "job_name";

    public static final String COL_JOB_CLASS_BEAN_NAME = "job_class_bean_name";

    public static final String COL_SHARDING_TOTAL_COUNT = "sharding_total_count";

    public static final String COL_SHARDING_ITEM_PARAMS = "sharding_item_params";

    public static final String COL_CRON_EXPRESSION = "cron_expression";

    public static final String COL_JOB_STATUS = "job_status";

    public static final String COL_REMARK = "remark";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_USER = "update_user";

    public static final String COL_UPDATE_TIME = "update_time";
}