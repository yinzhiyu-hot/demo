package cn.wangoon.domain.common;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description bean转换
 * @Remark
 * @PackagePath cn.wangoon.domain.common.BeanToBean
 * @Author YINZHIYU
 * @Date 2021/1/12 11:19
 * @Version 1.0.0.0
 **/
@Mapper
public interface BeanToBean {
    BeanToBean INSTANCE = Mappers.getMapper(BeanToBean.class);
}
