package com.carsonlius.module.system.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carsonlius.framework.mybatis.core.dataobject.BaseTenantDO;
import lombok.*;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:45
 * @company
 * @description
 */
@TableName("system_role_menu")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuDO extends BaseTenantDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;
}
