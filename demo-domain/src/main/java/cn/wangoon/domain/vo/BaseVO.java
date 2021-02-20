package cn.wangoon.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id修改时必填", required = true)
    protected Long id;

    @ApiModelProperty(value = "创建人id")
    protected Long createById;

    @ApiModelProperty(value = "创建人名称")
    protected String createByName;

    @ApiModelProperty(value = "创建人时间")
    protected LocalDateTime createTime;

    @ApiModelProperty(value = "修改人id")
    protected Long modifyById;

    @ApiModelProperty(value = "修改人名称")
    protected String modifyByName;

    @ApiModelProperty(value = "修改人时间")
    protected LocalDateTime modifyTime;
}
