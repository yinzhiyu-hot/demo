package cn.wangoon.service.business.base.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.dao.mapper.SysLogMapper;
import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.entity.SysLog;
import cn.wangoon.service.business.base.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description 日志记录
 * @Remark
 * @PackagePath cn.wangoon.service.business.impl.SysLogServiceImpl
 * @Author YINZHIYU
 * @Date 2020/9/27 10:32
 * @Version 1.0.0.0
 **/
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public void recordLog(SysLog sysLog) {

        ThreadUtil.execAsync(() -> {
            try {
                baseMapper.insert(sysLog);
            } catch (Exception ex) {
                LogUtils.error("记录日志异常", sysLog, ex);
            }
        });
    }

    @Override
    public void dataCarryForward(String recordDate) {
        int count = baseMapper.dataCarryForward(recordDate);
        recordLog(new SysLog("dataCarryForward", String.format("本次结转sys_log日志表数据记录 %s 条", count)));
    }

    @Override
    public Result<Object> recordLog(String json) {
        try {
            SysLog sysLog = JSONUtil.toBean(json, SysLog.class);
            recordLog(sysLog);
        } catch (Exception e) {
            return Result.exception(String.format("日志记录异常：%s", e));
        }
        return Result.ok();
    }
}
