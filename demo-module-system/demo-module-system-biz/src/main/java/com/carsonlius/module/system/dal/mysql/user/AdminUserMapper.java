package com.carsonlius.module.system.dal.mysql.user;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 10:58
 * @company
 * @description
 */
@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {
    default AdminUserDO selectByUsername(String username) {
        Wrapper<AdminUserDO> queryWrapper = new QueryWrapper<AdminUserDO>().eq("username", username)
                .eq("deleted", 0)
                .eq("tenant_id", 1);

        return selectOne(queryWrapper);
    }
}
