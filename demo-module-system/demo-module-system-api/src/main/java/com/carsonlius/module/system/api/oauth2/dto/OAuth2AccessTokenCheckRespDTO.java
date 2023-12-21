package com.carsonlius.module.system.api.oauth2.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/21 9:11
 * @company
 * @description OAuth2.0 访问令牌的校验 Response DTO
 */
@Data
public class OAuth2AccessTokenCheckRespDTO implements Serializable {

    // 用户ID
    private Long userId;

    // 租户类型
    private Integer userType;

    // 租户编号
    private Long  tenantId;

    // 授权范围
    List<String> scopes;
}
