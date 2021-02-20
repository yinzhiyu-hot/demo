package cn.wangoon.common.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * @Description 钉钉消息通知枚举
 * @Remark
 * @PackagePath cn.wangoon.common.enums.DingDingNoticeEnum
 * @Author YINZHIYU
 * @Date 2020/9/24 9:13
 * @Version 1.0.0.0
 **/
public enum DingDingNoticeEnum {
    BUSINESS("业务通知", 0),
    DEVELOP("研发通知", 1);

    // 成员变量
    private String remark;

    // 删除标记
    private Integer flag;

    // 构造方法
    DingDingNoticeEnum(String remark, Integer flag) {
        this.remark = remark;
        this.flag = flag;
    }

    public static String getRemark(Integer flag) {
        for (DingDingNoticeEnum dingDingNoticeEnum : DingDingNoticeEnum.values()) {
            if (Objects.equals(dingDingNoticeEnum.getFlag(), flag)) {
                return dingDingNoticeEnum.getRemark();
            }
        }
        return StrUtil.EMPTY;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
