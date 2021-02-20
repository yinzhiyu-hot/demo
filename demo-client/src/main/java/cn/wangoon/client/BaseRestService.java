package cn.wangoon.client;

import cn.wangoon.common.config.RestConfig;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description 基础Rest
 * @PackagePath cn.wangoon.service.rest.BaseRestService
 * @Author YINZHIYU
 * @Date 2020-04-14 09:09:00
 * @Version 1.0.0.0
 **/
@Component
public class BaseRestService {

    protected static final OkHttpClient client;

    static {
        client = new OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(30, TimeUnit.SECONDS).build();
    }

    @Resource
    public RestConfig restConfig;

    /*
     * @Description 创建API 的调用Service
     * @Remark
     * @Params ==>
     * @Param url 调用地址
     * @Param restApiClass 调用API Class
     * @Param gson gson配置
     * @Return T 返回对应的API
     * @Date 2020/8/19 18:16
     * @Auther YINZHIYU
     */
    public <T> T BuildService(String url, Class<T> restApiClass, Gson gson) {
        if (gson == null) {
            gson = new Gson();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(restApiClass);
    }
}
