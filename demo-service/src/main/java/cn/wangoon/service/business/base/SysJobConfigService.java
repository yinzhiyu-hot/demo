package cn.wangoon.service.business.base;

import cn.wangoon.domain.entity.SysJobConfig;
import cn.wangoon.domain.query.SysJobConfigQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

/**
 * @Description JOB调度配置业务类
 * @PackagePath cn.wangoon.service.business.SysJobConfigService
 * @Author YINZHIYU
 * @Date 2020-04-13 10:29:00
 * @Version 1.0.0.0
 **/
@Component
public interface SysJobConfigService extends IService<SysJobConfig> {
    SysJobConfig getSysJobConfigByCondition(SysJobConfigQuery sysJobConfigQuery);
}
