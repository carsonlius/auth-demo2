package com.carsonlius.module.system.api.permission;

import com.carsonlius.module.system.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:12
 * @company
 * @description 权限验证
 */
@Service
public class PermissionApiImpl implements PermissionApi {

    @Autowired
    private PermissionService permissionService;


    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }
}
