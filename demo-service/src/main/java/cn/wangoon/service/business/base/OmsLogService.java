package cn.wangoon.service.business.base;

import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

/**
 * @Description 日志
 * @Remark
 * @PackagePath cn.wangoon.service.business.OmsLogService
 * @Author YINZHIYU
 * @Date 2020/9/27 10:32
 * @Version 1.0.0.0
 **/
@Component
public interface OmsLogService extends IService<SysLog> {

    void recordLog(SysLog sysLog);

    void dataCarryForward(String recordDate);

    Result<Object> recordLog(String json);
}
