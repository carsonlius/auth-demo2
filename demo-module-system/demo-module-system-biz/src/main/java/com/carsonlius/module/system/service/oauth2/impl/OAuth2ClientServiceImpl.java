package com.carsonlius.module.system.service.oauth2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carsonlius.framework.common.enums.CommonStatusEnum;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.framework.common.util.string.StrUtils;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.carsonlius.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import com.carsonlius.module.system.enums.ErrorCodeConstants;
import com.carsonlius.module.system.service.oauth2.OAuth2ClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 14:05
 * @company
 * @description
 */
@Service
@Slf4j
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    @Autowired
    private OAuth2ClientMapper oauth2ClientMapper;

    @Override
    public OAuth2ClientDO validOAuthClient(String clientId, String clientSecret, String authorizedGrantType, Collection<String> scopes, String redirectUri) {
        OAuth2ClientDO client = oauth2ClientMapper.selectByClientId(clientId);

        // 校验客户端存在、且开启
        if (client == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_NOT_EXISTS);
        }

        if (!CommonStatusEnum.ENABLE.getStatus().equals(client.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_DISABLE);
        }

        // 校验客户端密钥
        if (!StringUtils.isEmpty(clientSecret) && !clientSecret.equals(client.getSecret())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }

        // 校验授权方式
        if (!StringUtils.isEmpty(authorizedGrantType) && !client.getAuthorizedGrantTypes().contains(authorizedGrantType)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }

        // 校验授权范围
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(client.getScopes(), scopes)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_SCOPE_OVER);
        }

        // 验证回调地址
        if (StrUtil.isNotEmpty(redirectUri) && !StrUtils.startWithAny(redirectUri, client.getRedirectUris())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH);
        }

        return client;
    }
}
