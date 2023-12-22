package com.carsonlius.module.system.dal.mysql.permission;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.permission.RoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 14:31
 * @company
 * @description
 */
@Mapper
public interface RoleMapper extends BaseMapperX<RoleDO> {
    default List<RoleDO> selectByIds(Set<Long> roleIds) {

        Wrapper<RoleDO> wrapper = new QueryWrapper<RoleDO>().in("id", roleIds);
        return selectList(wrapper);
    }
}
