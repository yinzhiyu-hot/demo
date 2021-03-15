package cn.wangoon.controller;

import cn.wangoon.domain.common.BussinessBody;
import cn.wangoon.domain.common.Result;
import cn.wangoon.service.business.api.common.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description Api 通用接口
 * @Remark
 * @PackagePath cn.wangoon.controller.ApiController
 * @Author YINZHIYU
 * @Date 2020/11/3 11:03
 * @Version 1.0.0.0
 **/
@Api(tags = "Api 通用接口")
@Controller
@RequestMapping(value = "/api")
public class ApiController {

    @Resource
    private ApiService apiService;

    /**
     * @Description Api 通用执行接口
     * @Remark
     * @Params ==>
     * @Param bussinessBody
     * @Param request
     * @Param response
     * @Return cn.wangoon.domain.common.Result<java.lang.Object>
     * @Date 2020/11/17 14:07
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "Api 通用执行接口", notes = "Api 通用执行接口", httpMethod = "POST")
    @PostMapping("/execute")
    @ResponseBody
    public Result<Object> execute(@RequestBody @ApiParam(name = "Api 执行业务请求体", required = true) BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response) {
        return apiService.execute(bussinessBody, request, response);
    }

    /**
     * @Description Api 通用查询接口
     * @Remark
     * @Params ==>
     * @Param bussinessBody
     * @Param request
     * @Param response
     * @Return cn.wangoon.domain.common.Result<java.lang.Object>
     * @Date 2020/11/17 14:07
     * @Auther YINZHIYU
     */
    @ApiOperation(value = "Api 通用查询接口", notes = "Api 通用查询接口", httpMethod = "POST")
    @PostMapping("/query")
    @ResponseBody
    public Result<Object> query(@RequestBody @ApiParam(name = "Api 查询业务请求体", required = true) BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response) {
        return apiService.query(bussinessBody, request, response);
    }
}
