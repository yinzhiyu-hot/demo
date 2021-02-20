package cn.wangoon.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description 反射工具类
 * @PackagePath cn.wangoon.common.utils.ReflectionsUtils
 * @Author YINZHIYU
 * @Date 2020/6/16 16:48
 * @Version 1.0.0.0
 **/
@SuppressWarnings("unchecked")
@Component
public class ReflectionsUtils {

    /**
     * @Description 获取所有子类
     * springboot 打成jar后，包目录结构变化了，idea运行倒是可用，时间紧，不做过多研究了
     * @Params ==>
     * @Param clazz
     * @Param scanPackage
     * @Return java.util.List<java.lang.Class < ?>>
     * @Date 2020/6/16 17:06
     * @Auther YINZHIYU
     */
    public static List<Class<?>> getSuperClasses(Class clazz) {
        List<Class<?>> classArrayList = Lists.newArrayList();
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

            // For List.class, this will be a path to rt.jar.
            configurationBuilder.addUrls(ClasspathHelper.forClass(clazz));
            configurationBuilder.addScanners(new SubTypesScanner());

            //获取该路径下所有类
            Reflections reflections = new Reflections(configurationBuilder);
            //获取继承了BaseSimpleJob的所有类
            Set<Class<?>> classSet = reflections.getSubTypesOf(clazz);
            classArrayList.addAll(classSet);
        } catch (Exception e) {
            LogUtils.error(clazz, "获取所有子类异常", e);
        }
        return classArrayList;
    }

    /**
     * @Description 获取指定类的指定了beanName的子类的class Map
     * @Params ==>
     * @Param clazz
     * @Param scanPackage
     * @Return java.util.Map<java.lang.String, java.lang.Class < ?>>
     * @Date 2020/6/16 17:11
     * @Auther YINZHIYU
     */
    public static Map<String, Class<?>> getSuperClassMapForComponent(Class clazz) {
        Map<String, Class<?>> stringClassMap = Maps.newHashMap();
        try {
            List<Class<?>> classArrayList = Lists.newArrayList();

            classArrayList.addAll(getSuperClasses(clazz));

            for (Class<?> clazzTemp : classArrayList) {
                stringClassMap.put(clazzTemp.getAnnotation(Component.class).value(), clazzTemp);
            }
        } catch (Exception e) {
            LogUtils.error(clazz, "获取所有子类并根据类名转Map处理异常", e);
        }
        return stringClassMap;
    }
}
