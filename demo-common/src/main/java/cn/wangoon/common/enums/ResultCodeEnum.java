package cn.wangoon.common.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * @Description 结果Code枚举
 * @PackagePath cn.wangoon.common.enums.DelFlagEnum
 * @Author YINZHIYU
 * @Date 2020/4/26 12:10
 * @Version 1.0.0.0
 **/
public enum ResultCodeEnum {
    SUCCESS("成功", 0),
    FAIL("失败", 1);

    // 成员变量
    private String remark;

    // 删除标记
    private Integer code;

    // 构造方法
    ResultCodeEnum(String remark, Integer code) {
        this.remark = remark;
        this.code = code;
    }

    public static String getRemark(Integer code) {
        for (ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()) {
            if (Objects.equals(resultCodeEnum.getCode(), code)) {
                return resultCodeEnum.getRemark();
            }
        }
        return StrUtil.EMPTY;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
