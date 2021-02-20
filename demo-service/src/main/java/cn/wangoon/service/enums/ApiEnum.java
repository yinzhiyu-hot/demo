package cn.wangoon.service.enums;

import cn.hutool.core.util.StrUtil;
import cn.wangoon.service.business.base.impl.OmsLogServiceImpl;
import cn.wangoon.service.business.base.impl.SysBaseConfigServiceImpl;

/**
 * @Description 同步任务类型枚举
 * @Remark 使用通用的API，实现业务的方法入参要求是Json 格式的字符串，返回值要求是Result<Object>
 * @PackagePath cn.wangoon.service.rest.base.enums.ApiEnum
 * @Author YINZHIYU
 * @Date 2020/12/10 11:06
 * @Version 1.0.0.0
 **/
public enum ApiEnum {
    RECORD_LOG("记录日志", "RECORD_LOG", OmsLogServiceImpl.class, "recordLog"),
    LIST_BASE_CONFIG("查询基础配置", "LIST_BASE_CONFIG", SysBaseConfigServiceImpl.class, "listSysBaseConfig");

    // 成员变量
    private String remark;

    // 任务类型
    private String bizType;

    // 处理类
    private Class clazz;

    // 调度中配置的业务bean 对应的处理方法
    private String method;

    // 构造方法
    ApiEnum(String remark, String bizType, Class clazz, String method) {
        this.remark = remark;
        this.bizType = bizType;
        this.clazz = clazz;
        this.method = method;
    }

    public static String getRemark(String bizType) {
        for (ApiEnum apiEnum : ApiEnum.values()) {
            if (StrUtil.equals(apiEnum.getBizType(), bizType, true)) {
                return apiEnum.getRemark();
            }
        }
        return StrUtil.EMPTY;
    }

    public static String getMethod(String bizType) {
        for (ApiEnum apiEnum : ApiEnum.values()) {
            if (StrUtil.equals(apiEnum.getBizType(), bizType, true)) {
                return apiEnum.getMethod();
            }
        }
        return StrUtil.EMPTY;
    }

    public static ApiEnum getApiEnum(String bizType) {
        for (ApiEnum apiEnum : ApiEnum.values()) {
            if (StrUtil.equals(apiEnum.getBizType(), bizType, true)) {
                return apiEnum;
            }
        }
        return null;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
