package com.carsonlius.framework.security.core.service.impl;

import com.carsonlius.framework.security.core.service.SecurityPermissionService;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.module.system.api.permission.PermissionApi;
import lombok.RequiredArgsConstructor;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 13:58
 * @company
 * @description
 */
@RequiredArgsConstructor
public class SecurityFrameworkServiceImpl implements SecurityPermissionService {

    private final PermissionApi permissionApi;

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();

        if (userId == null) {
            return false;
        }
        return permissionApi.hasAnyPermissions(userId, permissions);
    }
}
