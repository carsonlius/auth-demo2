package com.carsonlius.module.system.service.permission.impl;

import com.carsonlius.framework.common.enums.RoleCodeEnum;
import com.carsonlius.module.system.dal.dataobject.permission.RoleDO;
import com.carsonlius.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:29
 * @company
 * @description 角色 Service 接口
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public boolean hasAnySuperAdmin(List<RoleDO> roleList) {
        return roleList.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getCode()));
    }
}
