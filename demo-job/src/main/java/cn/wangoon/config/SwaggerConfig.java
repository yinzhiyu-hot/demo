package cn.wangoon.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description Swagger 配置
 * @PackagePath cn.wangoon.config.SwaggerConfig
 * @Author YINZHIYU
 * @Date 2020/4/23 16:22
 * @Version 1.0.0.0
 **/
@Configuration
@EnableSwagger2 //开启Swagger2
@EnableSwaggerBootstrapUI //开启SwaggerBootstrapUI增强
public class SwaggerConfig {

    /**
     * 抄送人列表
     */
    @Value("${spring.application.name}")
    public String applicationName;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.wangoon.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName.toUpperCase() + " RESTful APIs")
                .description(applicationName.toUpperCase())
                .termsOfServiceUrl("http://localhost:8083/")
                .version("1.0.0.0")
                .build();
    }
}