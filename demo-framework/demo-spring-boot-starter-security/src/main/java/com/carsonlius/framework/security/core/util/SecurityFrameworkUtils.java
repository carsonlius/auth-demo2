package com.carsonlius.framework.security.core.util;


import cn.hutool.core.util.StrUtil;
import com.carsonlius.framework.common.enums.UserTypeEnum;
import com.carsonlius.framework.security.core.entity.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 17:59
 * @company
 * @description 安全服务工具类
 */
public class SecurityFrameworkUtils {

    public final static String AUTHORIZATION_BEARER = "Bearer";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";


    private SecurityFrameworkUtils() {
    }

    /**
     * 从header中提取token
     */
    public static String obtainAuthorization(HttpServletRequest request, String header) {
        String authorization = request.getHeader(header);
        if (StrUtil.isEmpty(authorization)) {
            return null;
        }

        if (!StrUtil.startWith(authorization, AUTHORIZATION_BEARER + " ")) {
            return null;
        }

        return authorization.substring(AUTHORIZATION_BEARER.length() + 1).trim();
    }

    /**
     * 获取当前用户类型
     */
    public static Integer getLoginUserType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Integer userType = (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
        if (userType != null) {
            return userType;
        }

        return UserTypeEnum.ADMIN.getValue();
    }

    /**
     * 设置用户
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        /**
         * SecurityContextHolder.getContext().setAuthentication(authentication) 是 Spring Security 中用于设置当前用户认证信息的方法。
         * 它的作用是将给定的 Authentication 对象设置为当前线程的安全上下文中，表示当前用户已经通过身份验证。
         * */
        Authentication authentication = buildAuthentication(loginUser,request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    /**
     * 构建Authentication
     * */
    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        /**
         * 创建 UsernamePasswordAuthenticationToken 对象
         * public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
         *         Collection<? extends GrantedAuthority> authorities);
         * principal: 表示身份验证的主体，通常是用户名或用户实体。
         * credentials: 表示凭证，通常是密码或其他用于验证身份的凭据。
         * authorities: 表示用户的权限集合，通常是一组 GrantedAuthority 对象。
         * 举个例子
         * // 定义用户信息
         * String username = "user";
         * String password = "password";
         *
         * // 定义用户权限
         * Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
         *
         * // 创建 UsernamePasswordAuthenticationToken 实例
         * UsernamePasswordAuthenticationToken authenticationToken =
         *         new UsernamePasswordAuthenticationToken(username, password, authorities);
         * */
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authentication;
    }
}
