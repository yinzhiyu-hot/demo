package cn.wangoon.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 钉钉消息
 * @Remark
 * @PackagePath cn.wangoon.domain.dto.DingDingMsgContent
 * @Author YINZHIYU
 * @Date 2020/8/7 11:06
 * @Version 1.0.0.0
 **/
@Data
public class DingDingMsgContent implements Serializable {
    public String content;
}