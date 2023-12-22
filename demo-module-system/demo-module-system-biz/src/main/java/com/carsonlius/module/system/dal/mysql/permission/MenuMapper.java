package com.carsonlius.module.system.dal.mysql.permission;

import com.carsonlius.framework.mybatis.core.mapper.BaseMapperX;
import com.carsonlius.module.system.dal.dataobject.permission.MenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:32
 * @company
 * @description
 */
@Mapper
public interface MenuMapper extends BaseMapperX<MenuDO> {
    default List<MenuDO> selectByPermission(String permission) {
        return selectList(MenuDO::getPermission, permission);
    }
}
