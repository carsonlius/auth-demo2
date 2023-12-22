package com.carsonlius.module.system.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carsonlius.framework.mybatis.core.dataobject.BaseTenantDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:36
 * @company
 * @description
 */
@TableName("system_user_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleDO extends BaseTenantDO {
    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 角色 ID
     */
    private Long roleId;

}
