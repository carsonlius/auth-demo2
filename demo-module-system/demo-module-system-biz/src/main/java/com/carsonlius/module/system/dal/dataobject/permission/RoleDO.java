package com.carsonlius.module.system.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carsonlius.framework.mybatis.core.dataobject.BaseTenantDO;
import com.carsonlius.framework.mybatis.core.type.JsonLongSetTypeHandler;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:25
 * @company
 * @description 角色DO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_role")
@Builder
public class RoleDO extends BaseTenantDO {

    /**
     * 角色ID
     */
    @TableId
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色标识
     *
     * 枚举
     */
    private String code;
    /**
     * 角色排序
     */
    private Integer sort;
    /**
     * 角色状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 角色类型
     *
     * 枚举 {@link RoleTypeEnum}
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;

    /**
     * 数据范围
     *
     * 枚举 {@link DataScopeEnum}
     */
    private Integer dataScope;
//    /**
//     * 数据范围(指定部门数组)
//     *
//     * 适用于 {@link #dataScope} 的值为 {@link DataScopeEnum#DEPT_CUSTOM} 时
//     */
//    @TableField(typeHandler = JsonLongSetTypeHandler.class)
//    private Set<Long> dataScopeDeptIds;

}
