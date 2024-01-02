package com.carsonlius.framework.swagger.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/28 10:07
 * @company
 * @description swagger配置
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
public class DemoSwaggerAutoConfiguration {

    /**
     * 设置system分组
     * */
    @Bean
    public Docket createSystemApi(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("system")
                // 设置api信息
                .apiInfo(apiInfo(properties))
                // 扫描；controller包路径, 获取api接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.carsonlius.module.system"))
                .paths(PathSelectors.any())
                // 构建Docket对象
                .build()
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    /**
     * 设置system分组
     * */
    @Bean
    public Docket createWebApi(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("web")
                // 设置api信息
                .apiInfo(apiInfo(properties))
                // 扫描；controller包路径, 获取api接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.carsonlius.framework.web"))
                .paths(PathSelectors.any())
                // 构建Docket对象
                .build()
                .securitySchemes(Collections.singletonList(apiKey()));
    }


    /**
     * 设置请求头
     * */
    private springfox.documentation.service.ApiKey apiKey() {
        return new springfox.documentation.service.ApiKey("authorization", "Authorization", "header");
    }

    /**
     * 创建API信息
     * */
    public ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .contact(new Contact(swaggerProperties.getAuthor(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))
                .build();
    }
}
