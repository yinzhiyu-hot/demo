package cn.wangoon;

import cn.wangoon.common.utils.LogUtils;
import cn.wangoon.common.utils.SpringBootBeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @Description 应用程序启动入口
 * @PackagePath cn.wangoon.ApiApplication
 * @Author YINZHIYU
 * @Date 2020/6/15 14:42
 * @Version 1.0.0.0
 **/
@SpringBootApplication
public class ApiApplication {

    /**
     * 此处引用只为初始化 applicationContext
     */
    @Resource
    public SpringBootBeanUtil springBootBeanUtil;

    public static void main(String[] args) {
        try {
            SpringApplication.run(ApiApplication.class, args);

            LogUtils.info("系统启动完成");
        } catch (Exception e) {
            LogUtils.error(args, "SpringApplication.run(ApiApplication.class, args) ==> 异常", e);
            System.exit(0);//正常退出
            //System.exit(1);//强制退出
        }
    }
}
