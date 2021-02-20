package cn.wangoon.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.wangoon.common.annotations.ExcelProperty;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author rolker
 */
public class ExcelUtils {

    public static Map<Field, String> buildFiledMap(Class clazz) {
        Map<Field, String> filedMap = Maps.newLinkedHashMap();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (ObjectUtil.isNotNull(property)) {
                field.setAccessible(true);
                filedMap.put(field, property.excelTitle());

            }
        }
        return filedMap;
    }
}
