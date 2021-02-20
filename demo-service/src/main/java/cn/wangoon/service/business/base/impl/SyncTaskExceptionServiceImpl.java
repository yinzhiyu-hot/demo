package cn.wangoon.service.business.base.impl;

import cn.wangoon.dao.mapper.SyncTaskExceptionMapper;
import cn.wangoon.domain.entity.SyncTaskException;
import cn.wangoon.service.business.base.SyncTaskExceptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务异常信息 服务实现类
 * </p>
 *
 * @author yinzhiyu
 * @since 2019-10-14
 */
@Service
public class SyncTaskExceptionServiceImpl extends ServiceImpl<SyncTaskExceptionMapper, SyncTaskException> implements SyncTaskExceptionService {

}
