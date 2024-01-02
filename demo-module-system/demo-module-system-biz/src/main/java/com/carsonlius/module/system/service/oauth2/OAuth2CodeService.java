package com.carsonlius.module.system.service.oauth2;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2CodeDO;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 15:59
 * @company
 * @description OAuth2.0 授权码 Service 接口
 * 从功能上，和 Spring Security OAuth 的 JdbcAuthorizationCodeServices 的功能，提供授权码的操作
 */
public interface OAuth2CodeService {

    OAuth2CodeDO createAuthorizationCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state);
}
