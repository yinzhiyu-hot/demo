package cn.wangoon.client.dingding.api;

import cn.wangoon.domain.common.Result;
import cn.wangoon.domain.dto.DingDingMsgDto;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.util.Map;

/**
 * @Description 钉钉 API接口调用定义
 * @Remark
 * @PackagePath cn.wangoon.service.rest.base.api.DingDingRestApi
 * @Author YINZHIYU
 * @Date 2020/8/7 11:00
 * @Version 1.0.0.0
 **/
@Component
public interface DingDingRestApi {
    @POST("/dingtalk-message/api/notice")
    Call<Result<String>> dingdingNotice(@HeaderMap Map<String, String> headers, @Body DingDingMsgDto dto);
}