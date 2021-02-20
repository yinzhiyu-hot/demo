package cn.wangoon.dao.mapper;

import cn.wangoon.domain.entity.SysBaseConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 基础信息配置实体
 * @PackagePath cn.wangoon.dao.mapper.SysBaseConfigMapper
 * @Author YINZHIYU
 * @Date 2020/4/26 9:56
 * @Version 1.0.0.0
 **/
public interface SysBaseConfigMapper extends BaseMapper<SysBaseConfig> {

    int updateBatch(List<SysBaseConfig> list);

    int batchInsert(@Param("list") List<SysBaseConfig> list);

    int insertOrUpdate(SysBaseConfig record);

    int insertOrUpdateSelective(SysBaseConfig record);

    List<SysBaseConfig> listSysBaseConfigByCondition(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}