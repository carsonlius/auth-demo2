package com.carsonlius.framework.security.core.handler;

import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 16:09
 * @company
 * @description AccessDeniedHandler 接口是 Spring Security 中用于处理访问被拒绝（Access Denied）的情况的接口。
 * 当经过身份验证的用户尝试访问某个受保护的资源，但由于其角色或权限不足而被拒绝访问时，AccessDeniedHandler 接口的实现类将会被调用，
 * 允许应用程序自定义如何处理这种访问拒绝的情况。
 */
@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("[commence][访问 URL({}) 时，用户权限不够]", request.getRequestURI());
        // 返回 403
        ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN));
    }
}
