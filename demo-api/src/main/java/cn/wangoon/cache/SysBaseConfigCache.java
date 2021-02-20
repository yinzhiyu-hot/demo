package cn.wangoon.cache;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.wangoon.common.cache.BaseCache;
import cn.wangoon.common.constants.SysBaseConfigConstants;
import cn.wangoon.common.enums.DelFlagEnum;
import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.domain.entity.SysBaseConfig;
import cn.wangoon.service.business.base.SysBaseConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @Description 基础配置缓存
 * @PackagePath cn.wangoon.service.cache.impl.SysBaseConfigCache
 * @Author YINZHIYU
 * @Date 2020/4/26 10:45
 * @Version 1.0.0.0
 **/
@SuppressWarnings("unchecked")
@Component
public class SysBaseConfigCache implements BaseCache {

    /**
     * sys_base_config 配置map
     * KEY -> biz_type  Value Map for bizType
     */
    public final static ConcurrentMap<String, Map<String, List<SysBaseConfig>>> sysBaseConfigMap = Maps.newConcurrentMap();

    @Resource
    private SysBaseConfigService sysBaseConfigService;

    @Override
    public void init() {
        LogUtils.info("加载sysBaseConfigMap");
        QueryWrapper<SysBaseConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysBaseConfig.COL_DEL_FLAG, DelFlagEnum.NO.getFlag());
        List<SysBaseConfig> sysBaseConfigs = sysBaseConfigService.list(queryWrapper);
        convertMap(sysBaseConfigs);
    }

    /*
     * @Description 转换map
     * @Params ==>
     * @Param sysBaseConfigs
     * @Return void
     * @Date 2020/4/26 11:01
     * @Auther YINZHIYU
     */
    private void convertMap(List<SysBaseConfig> sysBaseConfigs) {
        if (ObjectUtil.isNotEmpty(sysBaseConfigs)) {

            sysBaseConfigMap.clear();

            //biz_type 分组
            Map<String, List<SysBaseConfig>> listBizTypeMap = sysBaseConfigs.stream().collect(Collectors.groupingBy(SysBaseConfig::getBizType));

            for (Map.Entry entry : listBizTypeMap.entrySet()) {
                //biz_key分组
                Map<String, List<SysBaseConfig>> listBizKeyMap = listBizTypeMap.get(entry.getKey().toString()).stream().collect(Collectors.groupingBy(SysBaseConfig::getBizKey));
                sysBaseConfigMap.put(entry.getKey().toString(), listBizKeyMap);
            }
        }
    }

    /*
     * @Description 获取单个配置值
     * @Params ==>
     * @Param bizType
     * @Param bizKey
     * @Param defaultValue
     * @Return T
     * @Date 2020/6/15 15:16
     * @Auther YINZHIYU
     */
    public static <T> T getSysBaseConfigFromGlobalMap(String bizType, String bizKey, T defaultValue) {

        Map<String, List<SysBaseConfig>> listMap = sysBaseConfigMap.get(bizType);
        if (ObjectUtil.isEmpty(listMap)) {
            return defaultValue;
        }
        List<SysBaseConfig> sysBaseConfigs = listMap.get(bizKey);
        if (ObjectUtil.isEmpty(sysBaseConfigs)) {
            return defaultValue;
        }
        SysBaseConfig sysBaseConfig = sysBaseConfigs.get(0);
        if (ObjectUtil.isEmpty(sysBaseConfig.getBizValue())) {
            return defaultValue;
        }
        if (!ClassUtil.getClass(defaultValue).isPrimitive()) {
            return ReflectUtil.invoke(defaultValue, "valueOf", sysBaseConfig.getBizValue());
        }
        return (T) sysBaseConfig.getBizValue();
    }

    /*
     * @Description 获取单个配置值（忽略bizType,bizKey大小写）
     * @Params ==>
     * @Param bizType
     * @Param bizKey
     * @Param defaultValue
     * @Return T
     * @Date 2020/6/15 15:16
     * @Auther YINZHIYU
     */
    public static <T> T getSysBaseConfigFromGlobalMapIgnoreCase(String bizType, String bizKey, T defaultValue) {
        Map<String, List<SysBaseConfig>> listMap = Maps.newHashMap();
        sysBaseConfigMap.forEach((key, value) -> {
            if (Objects.equals(key.toUpperCase(), bizType.toUpperCase())) {
                listMap.putAll(value);
            }
        });

        if (ObjectUtil.isEmpty(listMap)) {
            return defaultValue;
        }

        List<SysBaseConfig> sysBaseConfigs = Lists.newArrayList();
        listMap.forEach((key, value) -> {
            if (Objects.equals(key.toUpperCase(), bizKey.toUpperCase())) {
                sysBaseConfigs.addAll(value);
            }
        });

        if (ObjectUtil.isEmpty(sysBaseConfigs)) {
            return defaultValue;
        }
        SysBaseConfig sysBaseConfig = sysBaseConfigs.get(0);
        if (ObjectUtil.isEmpty(sysBaseConfig.getBizValue())) {
            return defaultValue;
        }
        if (!ClassUtil.getClass(defaultValue).isPrimitive()) {
            return ReflectUtil.invoke(defaultValue, "valueOf", sysBaseConfig.getBizValue());
        }
        return (T) sysBaseConfig.getBizValue();
    }

    /*
     * @Description 获取批量配置值
     * @Params ==>
     * @Param bizType
     * @Param bizKey
     * @Return java.util.List<cn.wangoon.domain.entity.SysBaseConfig>
     * @Date 2020/6/15 15:21
     * @Auther YINZHIYU
     */
    public static List<SysBaseConfig> getSysBaseConfigListFromGlobalMap(String bizType) {
        List<SysBaseConfig> sysBaseConfigs = Lists.newArrayList();

        Map<String, List<SysBaseConfig>> listMap = sysBaseConfigMap.get(bizType);
        if (ObjectUtil.isEmpty(listMap)) {
            return null;
        }
        listMap.values().forEach(sysBaseConfigs::addAll);

        if (ObjectUtil.isEmpty(sysBaseConfigs)) {
            return null;
        }
        return sysBaseConfigs;
    }

    /*
     * @Description 获取批量配置值
     * @Params ==>
     * @Param bizType
     * @Param bizKey
     * @Return java.util.List<cn.wangoon.domain.entity.SysBaseConfig>
     * @Date 2020/6/15 15:21
     * @Auther YINZHIYU
     */
    public static List<SysBaseConfig> getSysBaseConfigListFromGlobalMap(String bizType, String bizKey) {
        Map<String, List<SysBaseConfig>> listMap = sysBaseConfigMap.get(bizType);
        if (ObjectUtil.isEmpty(listMap)) {
            return null;
        }
        List<SysBaseConfig> sysBaseConfigs = listMap.get(bizKey);
        if (ObjectUtil.isEmpty(sysBaseConfigs)) {
            return null;
        }
        return sysBaseConfigs;
    }

    /*
     * @Description 获取批量配置值（忽略bizType,bizKey大小写）
     * @Params ==>
     * @Param bizType
     * @Param bizKey
     * @Return java.util.List<cn.wangoon.domain.entity.SysBaseConfig>
     * @Date 2020/6/15 15:21
     * @Auther YINZHIYU
     */
    public static List<SysBaseConfig> getSysBaseConfigListFromGlobalMapIgnoreCase(String bizType, String bizKey) {
        Map<String, List<SysBaseConfig>> listMap = Maps.newHashMap();
        sysBaseConfigMap.forEach((key, value) -> {
            if (Objects.equals(key.toUpperCase(), bizType.toUpperCase())) {
                listMap.putAll(value);
            }
        });

        if (ObjectUtil.isEmpty(listMap)) {
            return null;
        }

        List<SysBaseConfig> sysBaseConfigs = Lists.newArrayList();
        listMap.forEach((key, value) -> {
            if (Objects.equals(key.toUpperCase(), bizKey.toUpperCase())) {
                sysBaseConfigs.addAll(value);
            }
        });

        if (ObjectUtil.isEmpty(sysBaseConfigs)) {
            return null;
        }
        return sysBaseConfigs;
    }

    /**
     * @param bizType 获取所有type的集合
     * @return
     */
    public static List<SysBaseConfig> getAllBizType(String bizType) {
        Map<String, List<SysBaseConfig>> listMap = sysBaseConfigMap.get(bizType);
        List<SysBaseConfig> result = Lists.newArrayList();
        if (ObjectUtil.isNotEmpty(listMap)) {
            listMap.values().forEach(result::addAll);
        }
        return result;
    }

    /**
     * @Description 清洗前缀
     * @Remark
     * @PackagePath cn.wangoon.service.cache.impl.SysBaseConfigCache
     * @Author YINZHIYU
     * @Date 2020/11/10 17:53
     * @Version 1.0.0.0
     **/
    public static String cleaningPrefix(String str) {
        //获取基础配置中配置的需要移除前缀的后缀列表
        List<String> prefixRemoveList = new ArrayList<String>() {{
            List<SysBaseConfig> values = getSysBaseConfigListFromGlobalMap(SysBaseConfigConstants.PREFIX, SysBaseConfigConstants.PREFIX_REMOVE);
            if (ObjectUtil.isNotEmpty(values)) {
                values.forEach(sysBaseConfig -> this.add(sysBaseConfig.getBizValue()));
            }
        }};

        if (ObjectUtil.isNotEmpty(prefixRemoveList)) {
            for (String prefixRemove : prefixRemoveList) {
                if (StrUtil.indexOfIgnoreCase(str, prefixRemove) > -1) {
                    str = StrUtil.removePrefixIgnoreCase(str, prefixRemove);
                    break;
                }
            }
        }
        if (ObjectUtil.isNotEmpty(str)) {
            str = StrUtil.trimStart(str);
        }
        return str;
    }

    /**
     * @Description 清洗后缀
     * @Remark
     * @PackagePath cn.wangoon.service.cache.impl.SysBaseConfigCache
     * @Author YINZHIYU
     * @Date 2020/11/10 17:53
     * @Version 1.0.0.0
     **/
    public static String cleaningSuffix(String str) {
        //获取基础配置中配置的需要移除后缀的后缀列表
        List<String> suffixRemoveList = new ArrayList<String>() {{
            List<SysBaseConfig> values = getSysBaseConfigListFromGlobalMap(SysBaseConfigConstants.SUFFIX, SysBaseConfigConstants.SUFFIX_REMOVE);
            if (ObjectUtil.isNotEmpty(values)) {
                values.forEach(sysBaseConfig -> this.add(sysBaseConfig.getBizValue()));
            }
        }};

        if (ObjectUtil.isNotEmpty(suffixRemoveList)) {
            for (String suffixRemove : suffixRemoveList) {
                if (StrUtil.lastIndexOfIgnoreCase(str, suffixRemove) > -1) {
                    str = StrUtil.removeSuffixIgnoreCase(str, suffixRemove);
                    break;
                }
            }
        }
        if (ObjectUtil.isNotEmpty(str)) {
            str = StrUtil.trimEnd(str);
        }
        return str;
    }
}
