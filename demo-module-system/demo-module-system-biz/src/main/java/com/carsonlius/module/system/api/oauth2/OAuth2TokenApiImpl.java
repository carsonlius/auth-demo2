package com.carsonlius.module.system.api.oauth2;

import com.carsonlius.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.carsonlius.module.system.convert.auth.OAuth2TokenConvert;
import com.carsonlius.module.system.service.oauth2.OAuth2TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/21 9:17
 * @company
 * @description OAuth2.0 Token API 实现类
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        return OAuth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken));
    }
}
