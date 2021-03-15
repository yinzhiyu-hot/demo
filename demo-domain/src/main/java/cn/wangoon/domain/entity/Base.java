package cn.wangoon.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 基础单据bean
 * @Remark
 * @PackagePath cn.wangoon.domain.entity.Base
 * @Author YINZHIYU
 * @Date 2021/3/15 14:38
 * @Version 1.0.0.0
 **/
@Data
public class Base implements Serializable {
    /**
     * 异步任务-扩展备注
     * 加关键字transient(Gson 忽略掉该字段不进行转换),
     * 防止 API调用时,Gson转换Dto报多个orderNumber的异常导致API调用失败
     */
    public transient String extendRemark;
}
