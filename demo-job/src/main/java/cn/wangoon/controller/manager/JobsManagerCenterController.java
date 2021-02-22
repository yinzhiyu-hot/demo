package cn.wangoon.controller.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.cache.JobsConfigCache;
import cn.wangoon.common.config.NetConfig;
import cn.wangoon.config.JobsConfig;
import cn.wangoon.common.constants.RedisConstants;
import cn.wangoon.common.enums.JobStatusEnum;
import cn.wangoon.common.utils.CastUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.RedisUtils;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.entity.SysLog;
import cn.wangoon.domain.entity.SysJobConfig;
import cn.wangoon.domain.vo.BasePageVO;
import cn.wangoon.service.business.base.OmsLogService;
import cn.wangoon.service.business.base.SysJobConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description Job 管理中心
 * @PackagePath cn.wangoon.controller.JobsManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/4/27 18:00
 * @Version 1.0.0.0
 **/
@Api(tags = "Job 管理中心")
@Controller
@RequestMapping(value = "/jobs")
public class JobsManagerCenterController {

    @Resource
    RedisUtils redisUtils;

    @Resource
    private SysJobConfigService sysJobConfigService;

    @Resource
    private OmsLogService omsLogService;

    @Resource
    private JobsConfig jobsConfig;

    @Resource
    private NetConfig netConfig;

    /**
     * @Description Job配置页面
     * @Remark
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/11/17 14:02
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "Job配置页面", notes = "Job配置页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages() {
        return "jobs_manager";
    }

    /**
     * @Description 分页
     * @Remark
     * @Params ==>
     * @Param basePageVO
     * @Param sysJobConfig
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Date 2020/11/17 14:02
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "Job配置信息分页", notes = "Job配置信息分页", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {
        Page<SysJobConfig> page = new Page<>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SysJobConfig> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(sysJobConfig.getJobStatus())) {
            queryWrapper.eq(SysJobConfig.COL_JOB_STATUS, sysJobConfig.getJobStatus());
        }
        queryWrapper.orderByAsc(SysJobConfig.COL_JOB_NAME);
        IPage<SysJobConfig> pages = sysJobConfigService.getBaseMapper().selectPage(page, queryWrapper);
        //bootstrap-table要求服务器返回的json须包含：total，rows
        Map<String, Object> map = Maps.newHashMap();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        return map;
    }

    /**
     * @Description 启动
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "启动", notes = "启动", httpMethod = "POST")
    @PostMapping(value = "/start")
    @ResponseBody
    public Result start(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {

        try {
            //获取数据库调度配置信息
            SysJobConfig sysJobConfigLocal = sysJobConfigService.getById(sysJobConfig.getId());

            //启动调度
            JobsConfigCache.jobSchedulerMap.get(sysJobConfigLocal.getJobClassBeanName()).getSchedulerFacade().registerStartUpInfo(true);//是否启动

            //更新数据库状态
            sysJobConfigLocal.setJobStatus(JobStatusEnum.START.getStatus());
            sysJobConfigLocal.setUpdateTime(new Date());
            sysJobConfigService.updateById(sysJobConfigLocal);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);

            try {
                //获取Redis JOB 调度配置
                ConcurrentMap<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);

                omsLogService.recordLog(new SysLog(sysJobConfigLocal.getJobClassBeanName(), String.format("%s 启动成功。", sysJobConfigLocal.getJobClassBeanName())));

            } catch (Exception e) {
                LogUtils.error(sysJobConfig, "Job 启动异常", e);
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }

    /**
     * @Description JOB 停止
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ResponseBody
    @ApiOperation(value = "停止", notes = "停止", httpMethod = "POST")
    @PostMapping(value = "/stop")
    public Result stop(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {
        try {
            //获取数据库调度配置信息
            SysJobConfig sysJobConfigLocal = sysJobConfigService.getById(sysJobConfig.getId());

            //启动调度
            JobsConfigCache.jobSchedulerMap.get(sysJobConfigLocal.getJobClassBeanName()).getSchedulerFacade().registerStartUpInfo(false);//是否启动

            //更新数据库状态
            sysJobConfigLocal.setJobStatus(JobStatusEnum.STOP.getStatus());
            sysJobConfigLocal.setUpdateTime(new Date());
            sysJobConfigService.updateById(sysJobConfigLocal);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);

            try {
                //获取Redis JOB 调度配置
                ConcurrentMap<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfigLocal.getJobClassBeanName(), sysJobConfigLocal);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);

                omsLogService.recordLog(new SysLog(sysJobConfigLocal.getJobClassBeanName(), String.format("%s 停止成功。", sysJobConfigLocal.getJobClassBeanName())));

            } catch (Exception e) {
                LogUtils.error(sysJobConfig, "Job 停止异常", e);
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }

    /**
     * @Description JOB 更新
     * @Params ==>
     * @Param sysJobConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "更新", notes = "更新", httpMethod = "POST")
    @PostMapping(value = "/update")
    @ResponseBody
    public Result update(@RequestBody @ApiParam(name = "Job配置", required = true) SysJobConfig sysJobConfig) {

        try {
            sysJobConfig.setUpdateFlag(true);
            sysJobConfig.setUpdateIpPort(netConfig.getLocalIpPort());

            //更新调度配置信息
            ElasticJob elasticJob = CastUtil.cast(SpringBootBeanUtil.getBean(sysJobConfig.getJobClassBeanName()));
            LiteJobConfiguration liteJobConfiguration = jobsConfig.getLiteJobConfiguration(elasticJob, sysJobConfig.getCronExpression(), sysJobConfig.getShardingTotalCount(), sysJobConfig.getShardingItemParams());
            JobsConfigCache.jobSchedulerMap.get(sysJobConfig.getJobClassBeanName()).getSchedulerFacade().updateJobConfiguration(liteJobConfiguration);

            //更新数据库状态
            sysJobConfigService.updateById(sysJobConfig);

            //更新本地
            JobsConfigCache.sysJobConfigMap.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);

            try {
                //获取Redis JOB 调度配置
                ConcurrentMap<String, SysJobConfig> sysJobConfigMapRedis = redisUtils.getValue(RedisConstants.SYS_JOB_CONFIG_MAP_KEY);//取Redis 调度配置map

                //更新Redis
                sysJobConfigMapRedis.put(sysJobConfig.getJobClassBeanName(), sysJobConfig);
                redisUtils.set(RedisConstants.SYS_JOB_CONFIG_MAP_KEY, sysJobConfigMapRedis);

                omsLogService.recordLog(new SysLog(sysJobConfig.getJobClassBeanName(), String.format("%s 编辑成功。", sysJobConfig.getJobClassBeanName())));
            } catch (Exception e) {
                LogUtils.error(sysJobConfig, "Job 修改异常", e);
            }

            return Result.ok("执行成功");
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
    }
}
