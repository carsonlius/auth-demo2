package com.carsonlius.module.system.convert.auth;

import com.carsonlius.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/21 10:16
 * @company
 * @description
 */
@Mapper
public interface OAuth2TokenConvert {

    OAuth2TokenConvert INSTANCE = Mappers.getMapper(OAuth2TokenConvert.class);

    OAuth2AccessTokenCheckRespDTO convert(OAuth2AccessTokenDO bean);
}
