package com.carsonlius.module.system.service.oauth2.impl;

import cn.hutool.core.util.IdUtil;
import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.framework.common.util.date.DateUtils;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import com.carsonlius.module.system.dal.mysql.oauth2.OAuth2CodeMapper;
import com.carsonlius.module.system.enums.ErrorCodeConstants;
import com.carsonlius.module.system.service.oauth2.OAuth2CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/2 16:01
 * @company
 * @description
 */
@Service
@Slf4j
public class OAuth2CodeServiceImpl implements OAuth2CodeService {

    /**
     * 授权码的过期时间，默认 5 分钟
     */
    private static final Integer TIMEOUT = 5 * 60;

    @Autowired
    private OAuth2CodeMapper oAuth2CodeMapper;

    @Override
    public OAuth2CodeDO createAuthorizationCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state) {
        OAuth2CodeDO codeDo = new OAuth2CodeDO().setUserId(userId)
                .setCode(generateCode())
                .setClientId(clientId)
                .setUserType(userType)
                .setScopes(scopes)
                .setRedirectUri(redirectUri)
                .setExpiresTime(LocalDateTime.now().plusSeconds(TIMEOUT))
                .setState(state);
        codeDo.setCreateTime(LocalDateTime.now());
        codeDo.setUpdateTime(LocalDateTime.now());

        oAuth2CodeMapper.insert(codeDo);
        return codeDo;
    }

    @Override
    public OAuth2CodeDO consumeAuthorizationCode(String code) {
        //  查询code
        OAuth2CodeDO codeDO =  oAuth2CodeMapper.selectByCode(code);

        //  判空
        if (codeDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CODE_NOT_EXISTS);
        }

        // 判断是否过期
        if (DateUtils.isExpired(codeDO.getExpiresTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CODE_EXPIRE);
        }

        // 删除
        oAuth2CodeMapper.deleteById(codeDO.getId());

        return codeDO;
    }

    private static String generateCode() {
        return IdUtil.fastSimpleUUID();
    }
}
