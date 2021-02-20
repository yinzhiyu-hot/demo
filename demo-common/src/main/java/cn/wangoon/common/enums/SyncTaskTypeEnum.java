package cn.wangoon.common.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @Description 同步任务类型枚举
 * @Author YINZHIYU
 * @Date 2019-10-09 10:15:00
 * @Version 1.0.0.0
 **/
public enum SyncTaskTypeEnum {

    ALLOCATION_ORDER_WMS("订单分配 ==> Wms", "ALLOCATION_ORDER_WMS", "allocationOrderWms");
    // 成员变量
    private String remark;

    // 任务类型
    private String syncTaskType;

    // 调度中配置的业务bean 对应的处理方法
    private String method;

    // 构造方法
    SyncTaskTypeEnum(String remark, String syncTaskType, String method) {
        this.remark = remark;
        this.syncTaskType = syncTaskType;
        this.method = method;
    }

    public static String getRemark(String syncTaskType) {
        for (SyncTaskTypeEnum syncTaskTypeEnum : SyncTaskTypeEnum.values()) {
            if (StrUtil.equals(syncTaskTypeEnum.getSyncTaskType(), syncTaskType, true)) {
                return syncTaskTypeEnum.getRemark();
            }
        }
        return StrUtil.EMPTY;
    }

    public static String getMethod(String syncTaskType) {
        for (SyncTaskTypeEnum syncTaskTypeEnum : SyncTaskTypeEnum.values()) {
            if (StrUtil.equals(syncTaskTypeEnum.getSyncTaskType(), syncTaskType, true)) {
                return syncTaskTypeEnum.getMethod();
            }
        }
        return StrUtil.EMPTY;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSyncTaskType() {
        return syncTaskType;
    }

    public void setSyncTaskType(String syncTaskType) {
        this.syncTaskType = syncTaskType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
