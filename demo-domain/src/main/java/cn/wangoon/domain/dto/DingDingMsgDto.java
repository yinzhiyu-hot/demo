package cn.wangoon.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 钉钉消息dto
 * @Remark
 * @PackagePath cn.wangoon.domain.dto.DingDingMsgDto
 * @Author YINZHIYU
 * @Date 2020/8/7 11:02
 * @Version 1.0.0.0
 **/
@Data
public class DingDingMsgDto implements Serializable {
    /**
     * msg : {"content":"我就是我, 是不一样的烟火"}
     * agentId : 760634787
     * msgType : text
     * userIds :
     */
    public String msg;
    public int agentId;
    public String msgType;
    public String userIds;

    public String orderNumber;
}
