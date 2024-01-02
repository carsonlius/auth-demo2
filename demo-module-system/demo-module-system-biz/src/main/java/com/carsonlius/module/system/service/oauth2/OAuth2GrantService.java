package com.carsonlius.module.system.service.oauth2;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 15:18
 * @company
 * @description  OAuth2 授予 Service 接口
 *
 * 从功能上，和 Spring Security OAuth 的 TokenGranter 的功能，提供访问令牌、刷新令牌的操作
 *
 * 将自身的 AdminUser 用户，授权给第三方应用，采用 OAuth2.0 的协议。
 *
 * 问题：为什么自身也作为一个第三方应用，也走这套流程呢？
 * 回复：当然可以这么做，采用 password 模式。考虑到大多数开发者使用不到这个特性，OAuth2.0 毕竟有一定学习成本，所以暂时没有采取这种方式。
 */
public interface OAuth2GrantService {

    /**
     * 授权码模式, 第一阶段 获取code授权码
     * 对应 Spring Security OAuth2 的 AuthorizationEndpoint 的 generateCode 方法
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param scopes 授权范围
     * @param redirectUri 重定向 URI
     * @param state 状态
     * @return 授权码
     * */
    String grantAuthorizationCodeForCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state);
}
