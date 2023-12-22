package com.carsonlius.module.system.dal.mysql.permission;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.permission.RoleMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:46
 * @company
 * @description
 */
@Mapper
public interface RoleMenuMapper extends BaseMapperX<RoleMenuDO> {

    /**
     * 角色是否有指定菜单的权限
     */
    default  boolean menuInRoleIds(Set<Long> roleIds, Long menuId) {
        Wrapper<RoleMenuDO> wrapper = new QueryWrapper<RoleMenuDO>()
                .select("id")
                .in("role_id", roleIds)
                .eq("menu_id", menuId);

        RoleMenuDO menuDO = selectOne(wrapper);

        return !ObjectUtils.isEmpty(menuDO);
    }
}
