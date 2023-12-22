package com.carsonlius.module.system.api.permission;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:11
 * @company
 * @description 权限 API 接口
 */
public interface PermissionApi {
    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(Long userId, String... permissions);
}
