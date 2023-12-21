package com.carsonlius.module.system.api.oauth2;

import com.carsonlius.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/21 9:09
 * @company
 * @description OAuth2.0 Token API 接口
 */
public interface OAuth2TokenApi {
    OAuth2AccessTokenCheckRespDTO checkAccessToken(String token);
}
