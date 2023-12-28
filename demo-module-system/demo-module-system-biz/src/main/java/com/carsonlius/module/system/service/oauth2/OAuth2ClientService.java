package com.carsonlius.module.system.service.oauth2;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;

import java.util.Collection;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 14:03
 * @company
 * @description
 */
public interface OAuth2ClientService {


    /**
     * 校验客户端是否合法
     * */
    default OAuth2ClientDO validOAuthClient(String clientId) {
        return validOAuthClient(clientId, null, null, null, null);
    }

    /**
     * 校验客户端是否合法
     * <p>
     * 非空时，进行校验
     *
     * @param clientId            客户端编号
     * @param clientSecret        客户端密钥
     * @param authorizedGrantType 授权方式
     * @param scopes              授权范围
     * @param redirectUri         重定向地址
     * @return 客户端
     */
    OAuth2ClientDO validOAuthClient(String clientId, String clientSecret, String authorizedGrantType,
                                    Collection<String> scopes, String redirectUri);
}
