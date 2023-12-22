package com.carsonlius.framework.security.config;

import com.carsonlius.framework.security.core.filter.TokenAuthenticationFilter;
import com.carsonlius.framework.security.core.service.SecurityPermissionService;
import com.carsonlius.framework.security.core.service.impl.SecurityFrameworkServiceImpl;
import com.carsonlius.framework.web.core.handler.GlobalExceptionHandler;
import com.carsonlius.module.system.api.oauth2.OAuth2TokenApi;
import com.carsonlius.module.system.api.permission.PermissionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 17:20
 * @company
 * @description
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class DemoSecurityAutoConfiguration {


    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 账号密码登录需要使用
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
    }

    /**
     * token认证过滤器
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(SecurityProperties securityProperties, GlobalExceptionHandler globalExceptionHandler, OAuth2TokenApi oAuth2TokenApi) {
        return new TokenAuthenticationFilter(securityProperties, globalExceptionHandler, oAuth2TokenApi);
    }

    /**
     * 自定义权限校验服务
     * */
    @Bean("customPermission")
    public SecurityPermissionService securityFrameworkService(PermissionApi permissionApi) {
        return new SecurityFrameworkServiceImpl(permissionApi);
    }

}
