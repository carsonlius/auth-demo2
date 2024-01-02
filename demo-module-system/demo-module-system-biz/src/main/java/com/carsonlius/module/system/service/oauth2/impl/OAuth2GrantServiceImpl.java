package com.carsonlius.module.system.service.oauth2.impl;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import com.carsonlius.module.system.service.oauth2.OAuth2CodeService;
import com.carsonlius.module.system.service.oauth2.OAuth2GrantService;
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

    @Override
    public String grantAuthorizationCodeForCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state) {
        OAuth2CodeDO codeDo = oauth2CodeService.createAuthorizationCode(userId, userType, clientId, scopes, redirectUri, state);
        return codeDo.getCode();
    }
}
