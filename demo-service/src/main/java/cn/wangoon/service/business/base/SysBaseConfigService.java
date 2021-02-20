package cn.wangoon.service.business.base;

import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.entity.SysBaseConfig;
import cn.wangoon.domain.query.SysBaseConfigQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description 基础信息配置
 * @PackagePath cn.wangoon.service.business.SysBaseConfigService
 * @Author YINZHIYU
 * @Date 2020/4/26 9:56
 * @Version 1.0.0.0
 **/
public interface SysBaseConfigService extends IService<SysBaseConfig> {

    int updateBatch(List<SysBaseConfig> list);

    int batchInsert(List<SysBaseConfig> list);

    int insertOrUpdate(SysBaseConfig record);

    int insertOrUpdateSelective(SysBaseConfig record);

    List<SysBaseConfig> listSysBaseConfigByCondition(SysBaseConfigQuery sysBaseConfigQuery);

    Result<Object> listSysBaseConfig(String json);
}
