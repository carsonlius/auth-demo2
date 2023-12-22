package com.carsonlius.module.system.dal.mysql.permission;

import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.permission.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:39
 * @company
 * @description
 */
@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {

    default List<UserRoleDO> selectListByUserId(Long userId) {
        return selectList(UserRoleDO::getUserId, userId);
    }
}
