package com.carsonlius.framework.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 17:07
 * @company
 * @description Swagger 配置属性
 */
@ConfigurationProperties(prefix = "yudao.swagger")
@Data
public class SwaggerProperties {

    @NotEmpty(message="标题不能为空")
    private String title;

    @NotEmpty(message = "描述不能为空")
    private String description;

    @NotEmpty(message = "作者不能为空")
    private String author;

    @NotEmpty(message = "版本不能为空")
    private String version;

    @NotEmpty(message = "扫码的package不能为空")
    private String url;

    @NotEmpty(message = "扫描的email不能为空")
    private String email;

    @NotEmpty(message = "扫码的license不能为空")
    private String license;

    @NotEmpty(message = "扫描的 license-url 不能为空")
    private String licenseUrl;
}
