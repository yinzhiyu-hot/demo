package cn.wangoon.domain.common;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Description 分配参数
 * @PackagePath cn.wangoon.domain.common.ShardingItemParameters
 * @Author YINZHIYU
 * @Date 2020-04-14 10:58:00
 * @Version 1.0.0.0
 **/
@Data
public class ShardingItemParameters {
    public Map<String, String> map = Maps.newHashMap();

    public ShardingItemParameters(String shardingItemParameters) {
        if (ObjectUtil.isNotEmpty(shardingItemParameters)) {
            String[] kvs = shardingItemParameters.split("#");
            for (String kvStr : kvs) {
                String[] kv = kvStr.split(":");
                map.put(kv[0], kv[1]);
            }
        }
    }
}
