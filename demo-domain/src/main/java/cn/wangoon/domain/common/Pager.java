package cn.wangoon.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ApiModel(description = "分页响应信息主体")
public class Pager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码")
    private long current = 1;

    @ApiModelProperty(value = "每页条数")
    private long size = 20;

    @ApiModelProperty(value = "总页数")
    private long pages;

    @ApiModelProperty(value = "总条数")
    private long total;
}
