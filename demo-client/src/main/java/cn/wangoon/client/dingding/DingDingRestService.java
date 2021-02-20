package cn.wangoon.client.dingding;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.wangoon.client.BaseRestService;
import cn.wangoon.client.dingding.api.DingDingRestApi;
import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.dto.DingDingMsgDto;
import cn.wangoon.domain.entity.SysLog;
import cn.wangoon.service.business.base.OmsLogService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.http.HeaderMap;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 钉钉 Rest
 * @Remark
 * @PackagePath cn.wangoon.service.rest.base.DingDingRestService
 * @Author YINZHIYU
 * @Date 2020/8/7 10:59
 * @Version 1.0.0.0
 **/
@Component
public class DingDingRestService extends BaseRestService {

    @Resource
    private OmsLogService omsLogService;

    public Result<String> dingdingNotice(@HeaderMap Map<String, String> headers, DingDingMsgDto dto) {
        Gson gson = new GsonBuilder()
                .setDateFormat(DatePattern.NORM_DATETIME_PATTERN)
                .serializeNulls()
                .create();

        DingDingRestApi service = BuildService(restConfig.dingdingRestUrl, DingDingRestApi.class, gson);

        Response<Result<String>> response;
        try {
            response = service.dingdingNotice(headers, dto).execute();

            if (ObjectUtil.isNotEmpty(response.errorBody())) {
                String errorBodyStr = response.errorBody().string();
                omsLogService.recordLog(new SysLog(dto.getOrderNumber(), String.format("DingDingRestService ==> dingdingNotice ==> service.dingdingNotice(dto).execute() ==> 调用失败：%s -> %s，调用参数：%s", response, errorBodyStr, JSONUtil.toJsonStr(dto))));
                return Result.fail(errorBodyStr);
            }
        } catch (Exception e) {
            omsLogService.recordLog(new SysLog(dto.getOrderNumber(), String.format("DingDingRestService ==> dingdingNotice ==> 异常：%s", e)));
            return Result.exception(e.getMessage());
        }
        return response.body();
    }
}