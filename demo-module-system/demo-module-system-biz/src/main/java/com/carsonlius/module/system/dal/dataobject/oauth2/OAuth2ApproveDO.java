package com.carsonlius.module.system.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carsonlius.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.carsonlius.framework.common.enums.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:31
 * @company
 * @description OAuth2 批准 DO
 */
@TableName(value = "system_oauth2_approve")
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2ApproveDO extends BaseDO {
    /**
     * 编号，数据库自增
     */
    @TableId
    private Long id;
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
     *
     * 关联 {@link OAuth2ClientDO#getId()}
     */
    private String clientId;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 是否接受
     *
     * true - 接受
     * false - 拒绝
     */
    private Boolean approved;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;
}
