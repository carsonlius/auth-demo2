package com.carsonlius.module.system.service.oauth2.impl;

import cn.hutool.core.util.IdUtil;
import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.framework.common.util.date.DateUtils;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import com.carsonlius.module.system.dal.mysql.oauth2.OAuth2AccessTokenMapper;
import com.carsonlius.module.system.dal.mysql.oauth2.OAuth2RefreshTokenMapper;
import com.carsonlius.module.system.enums.ErrorCodeConstants;
import com.carsonlius.module.system.service.oauth2.OAuth2ClientService;
import com.carsonlius.module.system.service.oauth2.OAuth2TokenService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 13:53
 * @company
 * @description
 */
@Slf4j
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    @Autowired
    private OAuth2RefreshTokenMapper oAuth2RefreshTokenMapper;

    @Autowired
    private OAuth2AccessTokenMapper tokenMapper;

    @Override
    public OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
        // 获取client
        OAuth2ClientDO client = oAuth2ClientService.validOAuthClient(clientId, null, null, null, null);

        // 获取刷新token
        OAuth2RefreshTokenDO refreshToken = createOAuth2RefreshToken(userId, userType, client, scopes);

        //  获取token
        return createOAuth2AccessToken(client, refreshToken);
    }

    @Override
    public OAuth2AccessTokenDO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO tokenDO = getAccessToken(accessToken);

        // 校验访问令牌不存在
        if (tokenDO == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌不存在");
        }

        //  校验访问另外过期
        if (DateUtils.isExpired(tokenDO.getExpiresTime())) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌已过期");
        }

        return tokenDO;
    }

    @Override
    public OAuth2AccessTokenDO getAccessToken(String accessToken) {
        return tokenMapper.selectByAccessToken(accessToken);
    }

    /**
     * 生成token
     */
    private OAuth2AccessTokenDO createOAuth2AccessToken(OAuth2ClientDO client, OAuth2RefreshTokenDO refreshToken) {

        OAuth2AccessTokenDO token = OAuth2AccessTokenDO.builder()
                .accessToken(createToken())
                .refreshToken(refreshToken.getRefreshToken())
                .clientId(client.getClientId())
                .userId(refreshToken.getUserId())
                .userType(refreshToken.getUserType())
                .expiresTime(LocalDateTime.now().plusSeconds(client.getAccessTokenValiditySeconds()))
                .scopes(refreshToken.getScopes())
                .build();

        token.setCreateTime(LocalDateTime.now());
        token.setUpdateTime(LocalDateTime.now());
        tokenMapper.insert(token);

        return token;
    }

    /**
     * 创建刷新token
     */
    private OAuth2RefreshTokenDO createOAuth2RefreshToken(Long userId, Integer userType, OAuth2ClientDO client, List<String> scopes) {
        OAuth2RefreshTokenDO refreshToken = OAuth2RefreshTokenDO.builder()
                .refreshToken(createRefreshToken())
                .userId(userId)
                .userType(userType)
                .scopes(scopes)
                .clientId(client.getClientId())
                .expiresTime(LocalDateTime.now().plusSeconds(client.getRefreshTokenValiditySeconds()))
                .build();

        refreshToken.setCreateTime(LocalDateTime.now());
        refreshToken.setUpdateTime(LocalDateTime.now());

        oAuth2RefreshTokenMapper.insert(refreshToken);

        return refreshToken;
    }

    /**
     * 创建refreshToken
     */
    private String createRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 创建token
     */
    private String createToken() {
        return IdUtil.fastSimpleUUID();
    }
}
