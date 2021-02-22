package cn.wangoon;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import cn.wangoon.common.config.NetConfig;
import cn.wangoon.common.constants.RedisConstants;
import cn.wangoon.common.utils.RedisUtils;
import cn.wangoon.domain.entity.SysBaseConfig;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class AppApplicationSpringTests {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private NetConfig netConfig;

    @Test
    void testRedis() {
        List<SysBaseConfig> sysBaseConfigList = Lists.newArrayList();
        SysBaseConfig sysBaseConfig = new SysBaseConfig();
        sysBaseConfig.setBizKey("AAA");
        sysBaseConfig.setUpdateIpPort(netConfig.getLocalIpPort());

        SysBaseConfig sysBaseConfig2 = new SysBaseConfig();
        sysBaseConfig2.setBizKey("BBB");
        sysBaseConfig2.setUpdateIpPort(netConfig.getLocalIpPort());

        redisUtils.setForList(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig);
        redisUtils.setForList(RedisConstants.SYS_BASE_CONFIG_MAP_KEY, sysBaseConfig2);

        List<SysBaseConfig> sysBaseConfigList2 = redisUtils.getValue(RedisConstants.SYS_BASE_CONFIG_MAP_KEY);
        Console.log(JSONUtil.toJsonStr(sysBaseConfigList2));

    }
}
