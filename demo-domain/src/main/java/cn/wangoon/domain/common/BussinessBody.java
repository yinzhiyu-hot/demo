package cn.wangoon.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 通用的Api 请求体
 * @Remark
 * @PackagePath cn.wangoon.domain.common.BussinessBody
 * @Author YINZHIYU
 * @Date 2020/11/3 10:40
 * @Version 1.0.0.0
 **/
@Data
@ApiModel(description = "Api 接口调用请求体")
public class BussinessBody implements Serializable {

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "业务参数(字符串[普通/JSON]，约定俗成)")
    private String body;
}
