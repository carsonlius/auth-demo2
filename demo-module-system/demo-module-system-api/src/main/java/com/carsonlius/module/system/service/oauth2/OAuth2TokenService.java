package com.carsonlius.module.system.service.oauth2;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 10:17
 * @company
 * @description 从功能上，和 Spring Security OAuth 的 DefaultTokenServices + JdbcTokenStore 的功能，提供访问令牌、刷新令牌的操作
 */
public interface OAuth2TokenService {

    /**
     * 创建访问令牌
     * 注意：该流程中，会包含创建刷新令牌的创建
     *
     * 参考 DefaultTokenServices 的 createAccessToken 方法
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param scopes 授权范围
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes);


    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO checkAccessToken(String accessToken);


    /**
     * 获得访问令牌
     *
     * 参考 DefaultTokenServices 的 getAccessToken 方法
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO getAccessToken(String accessToken);

    /**
     * 刷新访问令牌
     * 参考 DefaultTokenServices 的 refreshAccessToken 方法
     * @param refreshAccessToken 刷新令牌
     * @param clientId 客户端编号
     * @return 访问令牌的信息
     * */
    OAuth2AccessTokenDO refreshAccessToken(String refreshAccessToken, String clientId);
}
