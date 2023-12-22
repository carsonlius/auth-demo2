package com.carsonlius.framework.security.core.service;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 13:57
 * @company
 * @description 权限验证接口
 */
public interface SecurityPermissionService {
    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String... permissions);
}
