package cn.wangoon.service.business.api.common;

import cn.wangoon.domain.common.BussinessBody;
import cn.wangoon.domain.common.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description API 接口定义
 * @Remark
 * @PackagePath cn.wangoon.service.rest.base.ApiService
 * @Author YINZHIYU
 * @Date 2020/11/3 10:19
 * @Version 1.0.0.0
 **/
@Service
public interface ApiService {

    Result<Object> execute(BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response);

    Result<Object> query(BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response);
}
