package cn.wangoon.common.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description Gson 工具类
 * @Remark
 * @PackagePath cn.wangoon.common.utils.GsonUtils
 * @Author YINZHIYU
 * @Date 2020/10/20 15:27
 * @Version 1.0.0.0
 **/
public class GsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gson = gsonBuilder.create();
        }
    }

    private GsonUtils() {
    }

    /**
     * @Description 转成json
     * @Remark
     * @Params ==>
     * @Param object
     * @Return java.lang.String
     * @Date 2020/10/20 15:27
     * @Auther YINZHIYU
     */
    public static String toJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * @Description 转成bean
     * @Remark
     * @Params ==>
     * @Param gsonString
     * @Param cls
     * @Return T
     * @Date 2020/10/20 15:26
     * @Auther YINZHIYU
     */
    public static <T> T toBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * @Description 转成list
     * @Remark 泛型在编译期类型被擦除导致报错
     * @Params ==>
     * @Param gsonString
     * @Param cls
     * @Return java.util.List<T>
     * @Date 2020/10/20 15:26
     * @Auther YINZHIYU
     */
    public static <T> List<T> toList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * @Description 转成list
     * @Remark 解决泛型问题
     * @Params ==>
     * @Param json
     * @Param cls
     * @Return java.util.List<T>
     * @Date 2020/10/20 15:26
     * @Auther YINZHIYU
     */
    @SuppressWarnings("deprecation")
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * @Description 转成list中有map的
     * @Remark
     * @Params ==>
     * @Param gsonString
     * @Return java.util.List<java.util.Map < java.lang.String, T>>
     * @Date 2020/10/20 15:25
     * @Auther YINZHIYU
     */
    public static <T> List<Map<String, T>> toListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * @Description 转成map的
     * @Remark
     * @Params ==>
     * @Param gsonString
     * @Return java.util.Map<java.lang.String, T>
     * @Date 2020/10/20 15:25
     * @Auther YINZHIYU
     */
    public static <T> Map<String, T> toMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}