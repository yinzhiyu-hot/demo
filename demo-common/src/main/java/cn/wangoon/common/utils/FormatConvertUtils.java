package cn.wangoon.common.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 格式转换工具类
 *
 * @author rolker
 */
public class FormatConvertUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * json字符串转出Map对象
     *
     * @param result
     * @return
     */
    public static Map convertStrToMap(String result) {
        JSONObject jsonObject = JSONUtil.parseObj(result);
        return JSONUtil.toBean(jsonObject, Map.class);
    }

    /**
     * 对象转成json字符串
     *
     * @param params
     * @return
     */
    public static String parseObjToStr(Object params) {
        String jsonParm = null;
        if (ObjectUtil.isNotEmpty(params)) {
            jsonParm = JSONUtil.toJsonPrettyStr(params);
        }
        return jsonParm;
    }


    /**
     * 普通对象转成map对象
     *
     * @param obj
     * @return
     */
    public static Map parseObjToMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }
}
