package com.carsonlius.framework.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.carsonlius.framework.common.exception.ErrorCode;
import com.carsonlius.framework.common.exception.ServiceException;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.common.util.servlet.ServletUtils;
import com.carsonlius.framework.security.config.SecurityProperties;
import com.carsonlius.framework.security.core.entity.LoginUser;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.framework.web.core.handler.GlobalExceptionHandler;
import com.carsonlius.module.system.api.oauth2.OAuth2TokenApi;
import com.carsonlius.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 17:28
 * @company
 * @description token验证器, 验证成功之后将用户信息存储到上下文中
 */
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final OAuth2TokenApi oAuth2TokenApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());

        if (StrUtil.isNotBlank(token)) {
            Integer userType = SecurityFrameworkUtils.getLoginUserType(request);

            try {
                // 基于token构建登录用户
                LoginUser loginUser = buildLoginUserByToken(token, userType);

                // 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }

            } catch (Throwable e) {
                log.error("token认证异常 {}", e);
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, e);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 根据token构建用户
     */
    private LoginUser buildLoginUserByToken(String token, Integer userType) {
        try {
            //  校验token
            OAuth2AccessTokenCheckRespDTO tokenCheckRespDTO = oAuth2TokenApi.checkAccessToken(token);

            if (tokenCheckRespDTO == null) {
                return null;
            }

            // 校验用户类型
            if (ObjectUtil.notEqual(tokenCheckRespDTO.getUserType(), userType)) {
                throw new AccessDeniedException("用户类型错误");
            }

            //  构建登录用户
            return LoginUser.builder()
                    .userType(userType)
                    .id(tokenCheckRespDTO.getUserId())
                    .tenantId(tokenCheckRespDTO.getTenantId())
                    .scopes(tokenCheckRespDTO.getScopes())
                    .build();
        } catch (ServiceException e) {

            log.error("token {} userType {} token认证异常 {}", token, userType, e.getMessage());
            throw e;
        }
    }
}
