package com.carsonlius.module.system.util;


import com.carsonlius.framework.common.util.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 14:32
 * @company
 * @description Oauth2相关的工具类
 */
public class OAuth2Utils {

    /**
     * 构建授权码模式下，重定向的 URI
     *
     * copy from Spring Security OAuth2 的 AuthorizationEndpoint 类的 getSuccessfulRedirect 方法
     *
     * @param redirectUri 重定向 URI
     * @param authorizationCode 授权码
     * @param state 状态
     * @return 授权码模式下的重定向 URI
     */
    public static String buildAuthorizationCodeRedirectUri(String redirectUri, String authorizationCode, String state) {
        Map<String, String> query = new HashMap<>();
        query.put("code", authorizationCode);

        if (!StringUtils.isEmpty(state)){
            query.put("state", state);
        }

        return HttpUtils.append(redirectUri, query, null, false);
    }


    /**
     * 构建错误的跳转链接响应
     */
    public static String buildUnsuccessfulRedirect(String redirectUrl, String responseType,
                                                   String state, String error, String description) {

        Map<String, String> query = new HashMap<>();
        query.put("error", error);
        query.put("error_description", description);
        if (state != null) {
            query.put("state", state);
        }

        return HttpUtils.append(redirectUrl, query, null, !responseType.contains("code"));
    }
}
