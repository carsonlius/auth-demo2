package com.carsonlius.module.system.service.oauth2;

import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:38
 * @company
 * @description OAuth2 批准 Service 接口, 从功能上，和 Spring Security OAuth 的 ApprovalStoreUserApprovalHandler 的功能，记录用户针对指定客户端的授权，减少手动确定。
 */
public interface OAuth2ApproveService {

    /**
     * 获得用户的批准列表，排除已过期的
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @return 是否授权通过
     */
    List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType,String clientId);

    /**
     * 用户发起授权申请时，基于scopes的选项 计算最总是否通过
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param requestedScopes 授权范围
     * @return 是否授权通过
     * */
    boolean updateAfterApproval(Long userId, Integer userType, String clientId, Map<String, Boolean> requestedScopes);

}
