package cn.wangoon.domain.query;

import cn.wangoon.domain.entity.SysBaseConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 * @Description 基础信息配置查询实体
 * @Remark
 * @Params ==>
 * @Param null
 * @Return
 * @Date 2020/12/14 11:54
 * @Auther YINZHIYU
 */
@ApiModel(value = "基础信息配置查询实体")
@Data
public class SysBaseConfigQuery extends SysBaseConfig implements Serializable {
    @ApiModelProperty(value = "业务配置键集合")
    List<String> bizKeyList;
    @ApiModelProperty(value = "业务类型集合")
    List<String> bizTypeList;
}