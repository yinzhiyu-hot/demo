package cn.wangoon.controller.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.cache.SysBaseConfigCache;
import cn.wangoon.common.config.NetConfig;
import cn.wangoon.common.constants.RedisConstants;
import cn.wangoon.common.enums.DelFlagEnum;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.RedisUtils;
import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.entity.SysBaseConfig;
import cn.wangoon.domain.query.SysBaseConfigQuery;
import cn.wangoon.domain.vo.BasePageVO;
import cn.wangoon.service.business.base.SysBaseConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
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
import java.util.Map;

/**
 * @Description 基础配置 管理中心
 * @PackagePath cn.wangoon.controller.BaseManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/4/27 18:00
 * @Version 1.0.0.0
 **/
@Api(tags = "基础配置 管理中心")
@Controller
@RequestMapping(value = "/bases")
public class BaseManagerCenterController {

    @Resource
    RedisUtils redisUtils;

    @Resource
    public SysBaseConfigCache sysBaseConfigCache;

    @Resource
    private SysBaseConfigService sysBaseConfigService;

    @Resource
    private NetConfig netConfig;

    /**
     * @Description 基础配置页面
     * @Remark
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/11/17 14:01
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "基础配置页面", notes = "基础配置页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages() {
        return "bases_manager";
    }

    /**
     * @Description 基础配置页面信息分页
     * @Remark
     * @Params ==>
     * @Param basePageVO
     * @Param sysBaseConfig
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Date 2020/11/17 14:01
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "基础配置页面信息分页", notes = "基础配置页面信息分页", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "基础配置", required = true) SysBaseConfig sysBaseConfig) {
        Page<SysBaseConfig> page = new Page<>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SysBaseConfig> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(sysBaseConfig.getBizType())) {
            queryWrapper.like(SysBaseConfig.COL_BIZ_TYPE, sysBaseConfig.getBizType());
        }
        if (ObjectUtil.isNotEmpty(sysBaseConfig.getBizKey())) {
            queryWrapper.like(SysBaseConfig.COL_BIZ_KEY, sysBaseConfig.getBizKey());
        }
        queryWrapper.eq(SysBaseConfig.COL_DEL_FLAG, DelFlagEnum.NO.getFlag());
        queryWrapper.orderByAsc(SysBaseConfig.COL_BIZ_TYPE, SysBaseConfig.COL_BIZ_KEY, SysBaseConfig.COL_BIZ_VALUE);
        IPage<SysBaseConfig> pages = sysBaseConfigService.getBaseMapper().selectPage(page, queryWrapper);
        //bootstrap-table要求服务器返回的json须包含：total，rows
        Map<String, Object> map = Maps.newHashMap();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        return map;
    }

    /**
     * @Description 基础配置 删除
     * @Params ==>
     * @Param sysBaseConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "删除", notes = "删除", httpMethod = "POST")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result delete(@RequestBody @ApiParam(name = "基础配置", required = true) SysBaseConfig sysBaseConfig) {
        boolean result;
        try {
            sysBaseConfig.setDelFlag(DelFlagEnum.YES.getFlag());
            result = sysBaseConfigService.updateById(sysBaseConfig);

            if (result) {
                sysBaseConfigCache.init();

                sysBaseConfig.setOperateDesc("删除");
                noticeSyncSysBaseConfigCache(sysBaseConfig);
            }
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
        if (result) {
            return Result.ok("执行成功");
        } else {
            return Result.fail("执行失败");
        }
    }

    /**
     * @Description 基础配置 更新
     * @Params ==>
     * @Param sysBaseConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "更新", notes = "更新", httpMethod = "POST")
    @PostMapping(value = "/update")
    @ResponseBody
    public Result update(@RequestBody @ApiParam(name = "基础配置", required = true) SysBaseConfig sysBaseConfig) {
        boolean result;
        try {
            result = sysBaseConfigService.updateById(sysBaseConfig);

            if (result) {
                sysBaseConfigCache.init();

                sysBaseConfig.setOperateDesc("更新");
                noticeSyncSysBaseConfigCache(sysBaseConfig);
            }
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
        if (result) {
            return Result.ok("执行成功");
        } else {
            return Result.fail("执行失败");
        }
    }

    /**
     * @Description 基础配置 新增
     * @Params ==>
     * @Param sysBaseConfig
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "新增", notes = "新增", httpMethod = "POST")
    @PostMapping(value = "/add")
    @ResponseBody
    public Result insert(@RequestBody @ApiParam(name = "基础配置", required = true) SysBaseConfig sysBaseConfig) {
        boolean result;
        try {
            result = SqlHelper.retBool(sysBaseConfigService.getBaseMapper().insert(sysBaseConfig));

            if (result) {
                sysBaseConfigCache.init();

                sysBaseConfig.setOperateDesc("新增");
                noticeSyncSysBaseConfigCache(sysBaseConfig);
            }
        } catch (Exception e) {
            return Result.exception(e.getMessage());
        }
        if (result) {
            return Result.ok("执行成功");
        } else {
            return Result.fail("执行失败");
        }
    }

    /**
     * @Description 基础配置查询
     * @Params ==>
     * @Param sysBaseConfigQuery
     * @Return cn.wangoon.domain.common.Result
     * @Date 2020/4/27 18:05
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "基础配置查询", notes = "基础配置查询", httpMethod = "POST")
    @PostMapping(value = "/listSysBaseConfig")
    @ResponseBody
    public Result listSysBaseConfig(@RequestBody @ApiParam(name = "基础配置查询参数", required = true) SysBaseConfigQuery sysBaseConfigQuery) {
        return Result.ok(sysBaseConfigService.listSysBaseConfigByCondition(sysBaseConfigQuery));
    }

    /**
     * @Description 通知同步基础配置到Redis, 带上标记，方便多实例的时候，其它实例能同步更新本地配置
     * @Params ==>
     * @Return void
     * @Date 2020/5/22 9:25
     * @Auther YINZHIYU
     */
    private void noticeSyncSysBaseConfigCache(SysBaseConfig sysBaseConfig) {
        sysBaseConfig.setUpdateFlag(true);
        sysBaseConfig.setUpdateIpPort(netConfig.getLocalIpPort());

        try {
            //更新Redis
            redisUtils.set(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig);
        } catch (Exception e) {
            LogUtils.error(sysBaseConfig, "通知同步基础配置到Redis ==> 操作Redis ==> 异常", e);
        }
    }
}
