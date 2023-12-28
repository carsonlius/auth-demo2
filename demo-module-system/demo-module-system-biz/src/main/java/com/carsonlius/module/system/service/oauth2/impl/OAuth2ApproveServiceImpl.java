package com.carsonlius.module.system.service.oauth2.impl;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.carsonlius.module.system.service.oauth2.OAuth2ApproveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:50
 * @company
 * @description OAuth2 批准 Service 实现类
 */
@Service
public class OAuth2ApproveServiceImpl implements OAuth2ApproveService {
    @Override
    public List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId) {
        // todo 实现
        return null;
    }
}
