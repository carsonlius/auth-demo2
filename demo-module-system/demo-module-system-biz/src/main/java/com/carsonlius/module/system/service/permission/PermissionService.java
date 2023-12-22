package com.carsonlius.module.system.service.permission;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:13
 * @company
 * @description
 */
public interface PermissionService {
    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(Long userId, String... permissions);
}
