package com.carsonlius.module.system.service.oauth2.impl;

import cn.hutool.core.util.StrUtil;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import com.carsonlius.module.system.enums.ErrorCodeConstants;
import com.carsonlius.module.system.service.oauth2.OAuth2CodeService;
import com.carsonlius.module.system.service.oauth2.OAuth2GrantService;
import com.carsonlius.module.system.service.oauth2.OAuth2TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 15:23
 * @company
 * @description OAuth2 授予 Service 实现类
 */
@Service
@Slf4j
public class OAuth2GrantServiceImpl implements OAuth2GrantService {

    @Autowired
    private OAuth2CodeService oauth2CodeService;

    @Autowired
    private OAuth2TokenService oAuth2TokenService;

    @Override
    public String grantAuthorizationCodeForCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state) {
        OAuth2CodeDO codeDo = oauth2CodeService.createAuthorizationCode(userId, userType, clientId, scopes, redirectUri, state);
        return codeDo.getCode();
    }

    @Override
    public OAuth2AccessTokenDO grantAuthorizationCodeForAccessToken(String clientId, String code, String redirectUri, String state) {
        //  获取oAuthCode
        OAuth2CodeDO codeDO = oauth2CodeService.consumeAuthorizationCode(code);

        //  校验clientID
        if (!codeDO.getClientId().equals(clientId)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_GRANT_CLIENT_ID_MISMATCH);
        }

        // 校验redirectUri
        if (!codeDO.getRedirectUri().equals(redirectUri)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_GRANT_REDIRECT_URI_MISMATCH);
        }


        // 校验state是否匹配
        state = StrUtil.nullToDefault(state, "");
        if (!state.equals(codeDO.getState())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_GRANT_STATE_MISMATCH);
        }

        // 创建访问令牌
        return oAuth2TokenService.createAccessToken(codeDO.getUserId(), codeDO.getUserType(), codeDO.getClientId(), codeDO.getScopes());
    }
}
