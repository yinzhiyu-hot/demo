package cn.wangoon.controller.manager;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.constants.SysBaseConfigConstants;
import cn.wangoon.domain.dto.SysLogDto;
import cn.wangoon.domain.entity.SysLog;
import cn.wangoon.domain.vo.BasePageVO;
import cn.wangoon.service.business.base.OmsLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description 日志 管理中心
 * @PackagePath cn.wangoon.controller.LogManagerCenterController
 * @Author YINZHIYU
 * @Date 2020/4/27 18:00
 * @Version 1.0.0.0
 **/
@Api(tags = "日志 管理中心")
@Controller
@RequestMapping(value = "/logs")
public class LogManagerCenterController {

    @Resource
    private OmsLogService omsLogService;

    /**
     * @Description 日志页面
     * @Remark
     * @Params ==>
     * @Param model
     * @Return java.lang.String
     * @Date 2020/11/17 14:06
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "日志页面", notes = "日志页面", httpMethod = "GET")
    @RequestMapping(value = "/pages")
    public String pages(Model model) {
        List<String> queryDateList = Lists.newArrayList();
        for (int i = 0; i < Math.abs(SysBaseConfigConstants.LOG_CARRY_FORWARD_DAY_OFFSET); i++) {
            String queryDate = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), i * -1), DatePattern.NORM_DATE_PATTERN);
            queryDateList.add(queryDate);
        }
        model.addAttribute("queryDateList", queryDateList);
        return "logs_manager";
    }

    /**
     * @Description 日志
     * @Remark
     * @Params ==>
     * @Param omsLogDto
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Date 2020/11/17 14:06
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "日志查询", notes = "日志查询", httpMethod = "GET")
    @RequestMapping(value = "/listPage")
    @ResponseBody
    public Map<String, Object> listPage(@ApiParam(name = "分页", required = true) BasePageVO basePageVO, @ApiParam(name = "日志", required = true) SysLogDto sysLogDto) {
        Page<SysLog> page = new Page<>(basePageVO.getPageNumber(), basePageVO.getPageSize());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysLog.COL_RECORD_DATE, sysLogDto.getQueryDate());

        if (ObjectUtil.isNotEmpty(sysLogDto.getHashKey())) {
            queryWrapper.eq(SysLog.COL_BUSINESS_KEY, sysLogDto.getHashKey());
        }
        queryWrapper.orderByDesc(SysLog.COL_TS);
        IPage<SysLog> pages = omsLogService.page(page, queryWrapper);
        //bootstrap-table要求服务器返回的json须包含：total，rows,采取客户端分页，服务端提供全部数据
        Map<String, Object> map = Maps.newHashMap();
        map.put("total", pages.getTotal());
        map.put("rows", pages.getRecords());
        return map;
    }
}
