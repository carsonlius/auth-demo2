package com.carsonlius.framework.security.core.handler;

import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 16:02
 * @company
 * @description AuthenticationEntryPoint 接口是 Spring Security 中的一个接口，
 * 用于处理在用户尝试访问受保护资源时出现的身份验证异常。它定义了一个方法 commence，该方法在身份验证失败时被调用，
 * 允许应用程序自定义处理方式，例如重定向到登录页面、返回特定的错误响应等。
 */
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI());
        // 返回 401
        ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.UNAUTHORIZED));
    }
}
