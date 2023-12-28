package com.carsonlius.framework.security.config;

import com.carsonlius.framework.security.core.filter.TokenAuthenticationFilter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.Map;
import java.util.Set;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 15:38
 * @company
 * @description 自定义的 Spring Security 配置适配器实现
 */
@AutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class DemoWebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 由于 Spring Security 创建 AuthenticationManager 对象时，没声明 @Bean 注解，导致无法被注入
     * 通过覆写父类的该方法，添加 @Bean 注解，解决该问题
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authentication) throws Exception {
        return authentication.getAuthenticationManager();
    }

    /**
     * 配置 URL 的安全配置
     * <p>
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 登出
        httpSecurity.cors().and() // 开启跨域
                .csrf().disable() // csrf禁用,因为不使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS（无状态）： 表示应用程序是无状态的，不会创建会话。这意味着每个请求都是独立的，不依赖于之前的请求。适用于 RESTful 风格的应用。
                .and().headers().frameOptions().disable()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint) // 身份未认证时响应
                .accessDeniedHandler(accessDeniedHandler); // 身份已经认证（登录）,但是没有权限的情况的响应


        Multimap<RequestMethod, String> permitAllUrlMap = getUrlsFormPermitAllAnnotation();
        // 设置具体请求的权限
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll() // 静态资源无需认证
                .antMatchers("/websocket/message").permitAll() // websocket无需认证
                .antMatchers("/swagger-ui.html", "/webjars/**", "/v2/api-docs", "/swagger-resources/**").permitAll()
                // 放行 Druid 监控的相关 URI
                .antMatchers("/druid/**").permitAll()
                .antMatchers(HttpMethod.GET, permitAllUrlMap.get(RequestMethod.GET).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST, permitAllUrlMap.get(RequestMethod.POST).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PUT, permitAllUrlMap.get(RequestMethod.PUT).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.DELETE, permitAllUrlMap.get(RequestMethod.DELETE).toArray(new String[0])).permitAll()
                .and().authorizeRequests().anyRequest().authenticated(); // 其他请求必须认证

        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    /**
     * 获取被PermitAll注解修饰的url地址
     * */
    private Multimap<RequestMethod, String> getUrlsFormPermitAllAnnotation() {
        Multimap<RequestMethod, String> methodMap = HashMultimap.create();

        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            HandlerMethod method = entry.getValue();
            if (!method.hasMethodAnnotation(PermitAll.class)) {
                continue;
            }
            if (entry.getKey().getPatternsCondition() == null) {
                continue;
            }

            Set<String> url = entry.getKey().getPatternsCondition().getPatterns();

            for (RequestMethod requestMethod : entry.getKey().getMethodsCondition().getMethods()) {
                methodMap.putAll(requestMethod, url);
            }
        }
        log.info("PermitAll注解修饰的urls {}", methodMap);

        return methodMap;
    }



}
