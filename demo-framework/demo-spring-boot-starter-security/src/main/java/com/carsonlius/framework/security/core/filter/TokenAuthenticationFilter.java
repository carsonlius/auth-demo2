package com.carsonlius.framework.security.core.filter;

import com.carsonlius.framework.security.config.SecurityProperties;
import com.carsonlius.framework.web.core.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
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
 * @description
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());



        filterChain.doFilter(request, response);
    }
}
