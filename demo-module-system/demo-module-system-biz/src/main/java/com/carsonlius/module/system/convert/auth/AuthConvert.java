package com.carsonlius.module.system.convert.auth;


import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessTokenDO bean);


}
