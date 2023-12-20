package com.carsonlius.module.system.dal.mysql.oauth2;

import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 14:08
 * @company
 * @description
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapperX<OAuth2ClientDO> {

    default OAuth2ClientDO selectByClientId(String clientId){
        return selectOne(OAuth2ClientDO::getClientId, clientId);
    }
}
