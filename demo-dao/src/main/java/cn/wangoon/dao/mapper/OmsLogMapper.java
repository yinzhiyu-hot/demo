package cn.wangoon.dao.mapper;

import cn.wangoon.domain.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description 日志记录
 * @Remark
 * @PackagePath cn.wangoon.dao.mapper.OmsLogMapper
 * @Author YINZHIYU
 * @Date 2020/9/27 10:32
 * @Version 1.0.0.0
 **/
public interface OmsLogMapper extends BaseMapper<SysLog> {

    int dataCarryForward(String recordDate);
}