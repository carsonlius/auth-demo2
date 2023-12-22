package com.carsonlius.module.system.service.permission.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.carsonlius.framework.common.enums.CommonStatusEnum;
import com.carsonlius.framework.common.util.collection.CollectionUtils;
import com.carsonlius.module.system.dal.dataobject.permission.MenuDO;
import com.carsonlius.module.system.dal.dataobject.permission.RoleDO;
import com.carsonlius.module.system.dal.dataobject.permission.UserRoleDO;
import com.carsonlius.module.system.dal.mysql.permission.MenuMapper;
import com.carsonlius.module.system.dal.mysql.permission.RoleMapper;
import com.carsonlius.module.system.dal.mysql.permission.RoleMenuMapper;
import com.carsonlius.module.system.dal.mysql.permission.UserRoleMapper;
import com.carsonlius.module.system.service.permission.PermissionService;
import com.carsonlius.module.system.service.permission.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:14
 * @company
 * @description 权限认证
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        // 权限为空, 表明不需要权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        List<RoleDO> roleList = getEnableUserRoleListByUserId(userId);
        if (CollectionUtil.isEmpty(roleList)) {
            return false;
        }

        // 遍历各个角色的权限, 只要任一角色有权限 则表示有权限
        for (String permission : permissions) {
            if (hasAnyPermission(roleList, permission)) {
                return true;
            }
        }

        // 超管也拥有权限
        return roleService.hasAnySuperAdmin(roleList);
    }

    /**
     * 角色是否有权限
     */
    private boolean hasAnyPermission(List<RoleDO> roleList, String permission) {
        // 获取权限对应的菜单
        List<MenuDO> menuList = menuMapper.selectByPermission(permission);

        // 没有对应菜单的则说明没有合法权限
        if (CollectionUtil.isEmpty(menuList)) {
            return false;
        }

        Set<Long> roleIds = CollectionUtils.convertSet(roleList, RoleDO::getId);
        for (MenuDO menuDO : menuList) {
            // 角色是否有这个菜单
            if (roleMenuMapper.menuInRoleIds(roleIds, menuDO.getId())) {
                return true;
            }
        }

        return false;
    }


    /**
     * 根据用户ID获取开启状态的角色列表
     */
    private List<RoleDO> getEnableUserRoleListByUserId(Long userId) {
        // 获取角色编号
        Set<Long> roleIdsSet = CollectionUtils.convertSet(userRoleMapper.selectListByUserId(userId), UserRoleDO::getRoleId);

        // 根据角色编号获取角色列表并移除不可用的角色
        List<RoleDO> roleList = roleMapper.selectByIds(roleIdsSet);
        roleList.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus()));
        return roleList;
    }


}
