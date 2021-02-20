package cn.wangoon.service.business.base.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.dao.mapper.SysJobConfigMapper;
import cn.wangoon.domain.entity.SysJobConfig;
import cn.wangoon.domain.query.SysJobConfigQuery;
import cn.wangoon.service.business.base.SysJobConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description JOB调度配置业务类
 * @PackagePath cn.wangoon.service.business.impl.SysJobConfigServiceImpl
 * @Author YINZHIYU
 * @Date 2020-04-13 10:29:00
 * @Version 1.0.0.0
 **/
@Service
public class SysJobConfigServiceImpl extends ServiceImpl<SysJobConfigMapper, SysJobConfig> implements SysJobConfigService {

    @Override
    public SysJobConfig getSysJobConfigByCondition(SysJobConfigQuery sysJobConfigQuery) {
        return getOne(convertQueryWrapper(sysJobConfigQuery), false);
    }

    private QueryWrapper<SysJobConfig> convertQueryWrapper(SysJobConfigQuery sysJobConfigQuery) {
        QueryWrapper<SysJobConfig> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(sysJobConfigQuery.getId())) {
            queryWrapper.eq(SysJobConfig.COL_ID, sysJobConfigQuery.getId());
        }
        if (ObjectUtil.isNotEmpty(sysJobConfigQuery.getJobClassBeanName())) {
            queryWrapper.eq(SysJobConfig.COL_JOB_CLASS_BEAN_NAME, sysJobConfigQuery.getJobClassBeanName());
        }
        if (ObjectUtil.isNotEmpty(sysJobConfigQuery.getJobName())) {
            queryWrapper.eq(SysJobConfig.COL_JOB_NAME, sysJobConfigQuery.getJobName());
        }
        if (ObjectUtil.isNotEmpty(sysJobConfigQuery.getJobStatus())) {
            queryWrapper.eq(SysJobConfig.COL_JOB_STATUS, sysJobConfigQuery.getJobStatus());
        }
        return queryWrapper;
    }
}
