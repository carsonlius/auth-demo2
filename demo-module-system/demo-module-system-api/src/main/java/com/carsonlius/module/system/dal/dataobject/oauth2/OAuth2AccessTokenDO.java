package com.carsonlius.module.system.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.carsonlius.framework.mybatis.core.dataobject.BaseDO;
import com.carsonlius.framework.mybatis.core.dataobject.BaseTenantDO;
import com.carsonlius.framework.mybatis.core.handler.StringListTypeHandler;
import com.carsonlius.framework.mybatis.core.handler.StringListTypeHandler2;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.carsonlius.framework.common.enums.UserTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OAuth2 访问令牌 DO
 *
 * 如下字段，暂时未使用，暂时不支持：
 * user_name、authentication（用户信息）
 *
 */
@TableName(value = "system_oauth2_access_token", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class OAuth2AccessTokenDO extends BaseTenantDO {

    /**
     * 编号，数据库递增
     */
    @TableId
    private Long id;
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 客户端编号
     tong
     * 关联 {@link OAuth2ClientDO#getId()}
     */
    private String clientId;
    /**
     * 授权范围
     */
    @TableField(typeHandler = StringListTypeHandler.class)
//    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

}
