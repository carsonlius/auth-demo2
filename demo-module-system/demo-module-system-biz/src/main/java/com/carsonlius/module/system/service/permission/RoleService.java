package com.carsonlius.module.system.service.permission;

import com.carsonlius.module.system.dal.dataobject.permission.RoleDO;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:28
 * @company
 * @description 角色 Service 接口
 */
public interface RoleService {

    /**
     * 判断角色编号数组中，是否有管理员
     *
     * @param ids 角色编号数组
     * @return 是否有管理员
     */
    boolean hasAnySuperAdmin(List<RoleDO> roleList);
}
