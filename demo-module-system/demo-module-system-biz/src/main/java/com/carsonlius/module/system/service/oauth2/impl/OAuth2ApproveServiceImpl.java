package com.carsonlius.module.system.service.oauth2.impl;

import com.carsonlius.framework.common.util.date.DateUtils;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.carsonlius.module.system.dal.mysql.oauth2.OAuth2ApproveMapper;
import com.carsonlius.module.system.service.oauth2.OAuth2ApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:50
 * @company
 * @description OAuth2 批准 Service 实现类
 */
@Service
public class OAuth2ApproveServiceImpl implements OAuth2ApproveService {

    /**
     * 批准的过期时间，默认 30 天, 单位：秒
     */
    private static final Integer TIMEOUT = 30 * 24 * 60 * 60;

    @Autowired
    private OAuth2ApproveMapper oAuth2ApproveMapper;

    @Override
    public List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId) {
        List<OAuth2ApproveDO> approvalList = oAuth2ApproveMapper.selectListByUserIdAndUserTypeAndClientId(userId, userType, clientId);
        approvalList.removeIf(approval -> DateUtils.isExpired(approval.getExpiresTime()));
        return approvalList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAfterApproval(Long userId, Integer userType, String clientId, Map<String, Boolean> requestedScopes) {
        // requestedScopes为空，则表示没有权限范围要求, 此时返回true
        if (CollectionUtils.isEmpty(requestedScopes)) {
            return true;
        }

        // 至少有一个权限
        boolean success = false;

        // 更新批准信息
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        for (Map.Entry<String, Boolean> entry : requestedScopes.entrySet()) {
            if (entry.getValue()) {
                success = true;
            }

            // 保存更新信息
            saveApprove(userId, userType, clientId, entry.getKey(), entry.getValue(), expireTime);
        }

        return success;
    }

    /**
     * 保存筛选的权限以及没有选中的权限
     * */
    private void saveApprove(Long userId, Integer userType, String clientId, String scope, Boolean approved, LocalDateTime expireTime) {
        OAuth2ApproveDO approveDO = (OAuth2ApproveDO) new OAuth2ApproveDO().setUserId(userId)
                .setUserType(userType)
                .setApproved(approved)
                .setScope(scope)
                .setClientId(clientId)
                .setExpiresTime(expireTime)
                .setUpdateTime(LocalDateTime.now());

        // 先更新， 更新失败 则说明不存在，需要插入
        if (oAuth2ApproveMapper.update(approveDO) == 1) {
            return;
        }

        oAuth2ApproveMapper.insert(approveDO);
    }
}
