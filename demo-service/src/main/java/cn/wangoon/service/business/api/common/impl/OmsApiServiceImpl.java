package cn.wangoon.service.business.api.common.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.utils.CastUtil;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import cn.wangoon.domain.common.BussinessBody;
import cn.wangoon.domain.common.Result;
import cn.wangoon.service.business.api.common.OmsApiService;
import cn.wangoon.service.enums.ApiEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @Description Oms API 接口实现bean
 * @Remark 适配对应方法
 * @PackagePath cn.wangoon.service.rest.base.impl.OmsApiServiceImpl
 * @Author YINZHIYU
 * @Date 2020/11/3 10:20
 * @Version 1.0.0.0
 **/
@Service
public class OmsApiServiceImpl implements OmsApiService {

    /*
     * @Description 统一执行入口
     * @Remark
     * @Params ==>
     * @Param bussinessBody
     * @Param request
     * @Param response
     * @Return cn.wangoon.domain.common.Result<java.lang.Object>
     * @Date 2020/12/10 9:45
     * @Auther YINZHIYU
     */
    @Override
    public Result<Object> execute(BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response) {

        Result<Object> result;

        try {

            // 程序开始时间
            Instant startTime = Instant.now();

            ApiEnum apiEnum = ApiEnum.getApiEnum(bussinessBody.getBizType());

            if (ObjectUtil.isEmpty(apiEnum)) {
                return Result.fail("缺失BizType参数");
            }

            result = adapter(apiEnum, bussinessBody.getBody());

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(String.format("%s, Api [%s]执行结束，执行%s，执行耗时 %s ms。", apiEnum.getRemark(), apiEnum.getMethod(), result.getSuccess() ? "成功" : "失败", millis));

        } catch (Exception e) {
            result = Result.exception(String.format("服务端接口执行异常，异常信息：%s", e));
        }
        return result;
    }

    /*
     * @Description 统一查询入口
     * @Remark
     * @Params ==>
     * @Param bussinessBody
     * @Param request
     * @Param response
     * @Return cn.wangoon.domain.common.Result<java.lang.Object>
     * @Date 2020/12/10 9:45
     * @Auther YINZHIYU
     */
    @Override
    public Result<Object> query(BussinessBody bussinessBody, HttpServletRequest request, HttpServletResponse response) {

        Result<Object> result;

        try {

            // 程序开始时间
            Instant startTime = Instant.now();

            ApiEnum apiEnum = ApiEnum.getApiEnum(bussinessBody.getBizType());

            if (ObjectUtil.isEmpty(apiEnum)) {
                return Result.fail("缺失BizType参数");
            }

            result = adapter(apiEnum, bussinessBody.getBody());

            // 程序结束时间
            Instant endTime = Instant.now();

            // 毫秒
            long millis = ChronoUnit.MILLIS.between(startTime, endTime);

            LogUtils.info(String.format("%s, Api [%s]查询结束，查询%s，查询耗时 %s ms。", apiEnum.getRemark(), apiEnum.getMethod(), result.getSuccess() ? "成功" : "失败", millis));

        } catch (Exception e) {
            result = Result.exception(String.format("服务端接口查询异常，异常信息：%s", e));
        }
        return result;
    }

    /*
     * @Description 业务适配
     * @Remark
     * @Params ==>
     * @Param apiEnum
     * @Param jsonContent
     * @Return cn.wangoon.domain.common.Result<java.lang.Object>
     * @Date 2020/12/10 9:46
     * @Auther YINZHIYU
     */
    protected Result<Object> adapter(ApiEnum apiEnum, String jsonContent) throws Exception {
        Method method = apiEnum.getClazz().getMethod(ApiEnum.getMethod(apiEnum.getBizType()), String.class);
        return CastUtil.cast(method.invoke(SpringBootBeanUtil.getBean(apiEnum.getClazz()), jsonContent));
    }
}
