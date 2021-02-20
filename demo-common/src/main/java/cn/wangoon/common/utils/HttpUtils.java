package cn.wangoon.common.utils;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 网络请求工具
 */
public class HttpUtils {

    /**
     * 请求post接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendPost(String url, String resultParams, Map<String, String> heads) {
        HttpRequest.setGlobalTimeout(1000000);
        HttpRequest request = HttpRequest.post(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }


    /**
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendPut(String url, String resultParams, Map<String, String> heads) {
        HttpRequest.setGlobalTimeout(1000000);
        HttpRequest request = HttpRequest.put(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }

    /**
     * 请求get接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendGet(String url, String resultParams, Map<String, String> heads) {
        HttpRequest.setGlobalTimeout(1000000);
        HttpRequest request = HttpRequest.get(url);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            request.body(resultParams);
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }

    /**
     * 请求get接口
     *
     * @param url
     * @param params
     * @param heads
     * @return
     */
    public static String sendGet(String url, Map<String, Object> params, Map<String, String> heads) {
        HttpRequest.setGlobalTimeout(1000000);
        HttpRequest request = HttpRequest.get(url);
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        if (ObjectUtil.isNotEmpty(params)) {
            request.form(params);
        }
        return request.execute().body();
    }

    /**
     * 请求post接口
     *
     * @param url
     * @param resultParams
     * @param heads
     * @return
     */
    public static String sendKeyValuePost(String url, Map<String, Object> resultParams, Map<String, String> heads) {
        HttpRequest.setGlobalTimeout(1000000);
        HttpRequest request = HttpRequest.post(url);
        request.setReadTimeout(1000000);
        if (ObjectUtil.isNotEmpty(resultParams)) {
            for (Map.Entry<String, Object> entry : resultParams.entrySet()) {
                request.form(entry.getKey(), entry.getValue());
            }
        }
        if (ObjectUtil.isNotEmpty(heads)) {
            request.headerMap(heads, true);
        }
        return request.execute().body();
    }

    /**
     * @Description 将url参数转换成map
     * @Remark
     * @Params ==>
     * @Param param
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Date 2020/9/28 9:13
     * @Auther YINZHIYU
     */
    public static Map<String, Object> getUrlParams(String param) {

        if (param.indexOf("?") == 0) {
            param = param.substring(1);
        }

        Map<String, Object> map = Maps.newHashMap();
        if (ObjectUtil.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String s : params) {
            String[] p = s.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * @Description 将map转换成url
     * @Remark
     * @Params ==>
     * @Param map
     * @Return java.lang.String
     * @Date 2020/9/28 9:13
     * @Auther YINZHIYU
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }
}
