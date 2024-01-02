package com.carsonlius.module.system.dal.mysql.oauth2;

import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 9:26
 * @company
 * @description
 */
@Mapper
public interface OAuth2ApproveMapper extends BaseMapperX<OAuth2ApproveDO> {

    default List<OAuth2ApproveDO> selectListByUserIdAndUserTypeAndClientId(Long userId, Integer userType, String clientId) {
        return selectList(new LambdaQueryWrapperX<OAuth2ApproveDO>()
                .eq(OAuth2ApproveDO::getUserId, userId)
                .eq(OAuth2ApproveDO::getClientId, clientId)
                .eq(OAuth2ApproveDO::getUserType, userType)
        );
    }
}
