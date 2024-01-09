package com.carsonlius.module.system.convert.oauth2;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.carsonlius.framework.common.core.KeyValue;
import com.carsonlius.framework.common.enums.UserTypeEnum;
import com.carsonlius.framework.common.util.collection.CollectionUtils;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAccessTokenRespVO;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenCheckTokenRespVO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.carsonlius.module.system.util.OAuth2Utils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:27
 * @company
 * @description
 */
@Mapper
public interface OAuth2OpenConvert {
    OAuth2OpenConvert INSTANCE = Mappers.getMapper(OAuth2OpenConvert.class);

    default OAuth2OpenAuthorizeInfoRespVO convert(OAuth2ClientDO client, List<OAuth2ApproveDO> approves) {
        // 构建 scopes
        List<KeyValue<String, Boolean>> scopes = new ArrayList<>(client.getScopes().size());
        Map<String, OAuth2ApproveDO> approveMap = CollectionUtils.convertMap(approves, OAuth2ApproveDO::getScope);
        client.getScopes().forEach(scope -> {
            OAuth2ApproveDO approve = approveMap.get(scope);
            scopes.add(new KeyValue<>(scope, approve != null ? approve.getApproved() : false));
        });
        // 拼接返回
        return new OAuth2OpenAuthorizeInfoRespVO(
                new OAuth2OpenAuthorizeInfoRespVO.Client(client.getName(), client.getLogo()), scopes);
    }
    OAuth2OpenAccessTokenRespVO convert0(OAuth2AccessTokenDO bean);

    default OAuth2OpenAccessTokenRespVO convert(OAuth2AccessTokenDO token){
        OAuth2OpenAccessTokenRespVO respVO = convert0(token);
        respVO.setTokenType(SecurityFrameworkUtils.AUTHORIZATION_BEARER.toLowerCase(Locale.ROOT));
        respVO.setScope(OAuth2Utils.buildScopeStr(token.getScopes()));
        respVO.setExpiresIn(OAuth2Utils.getExpiresIn(token.getExpiresTime()));
        return respVO;
    }

    OAuth2OpenCheckTokenRespVO convert3(OAuth2AccessTokenDO bean);

    default OAuth2OpenCheckTokenRespVO convert2(OAuth2AccessTokenDO tokenDO){
        OAuth2OpenCheckTokenRespVO respVO = convert3(tokenDO);
        respVO.setExp(LocalDateTimeUtil.toEpochMilli(tokenDO.getExpiresTime()) / 1000L);
        respVO.setUserType(UserTypeEnum.ADMIN.getValue());
        return respVO;
    }
}
