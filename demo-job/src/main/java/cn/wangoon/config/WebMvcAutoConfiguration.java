package cn.wangoon.config;

import cn.wangoon.common.utils.SpringBootBeanUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

/**
 * @Description WEB相关配置
 * @PackagePath cn.wangoon.config.WebMvcAutoConfiguration
 * @Author YINZHIYU
 * @Date 2020/6/15 14:43
 * @Version 1.0.0.0
 **/
@Configuration
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 拦截器组
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(SpringBootBeanUtil.getBean(LoginContextInterceptor.class));//OMS系统后台管理中心登录拦截器，线上环境访问需登录
    }

    /**
     * 设置资源文件目录
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/**").
                addResourceLocations("classpath:/public/");
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 设置允许跨域的路径
                .allowedOrigins("*")// 设置允许跨域请求的域名
                .allowCredentials(true)// 是否允许证书 不再默认开启
                .allowedMethods("GET", "POST", "PUT", "DELETE")// 设置允许的方法
                .maxAge(3600);// 跨域允许时间
    }
}