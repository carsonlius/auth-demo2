package com.carsonlius.module.system.service.user;

import com.carsonlius.module.system.dal.dataobject.user.AdminUserDO;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 10:33
 * @company
 * @description
 */
public interface AdminUserService {

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    AdminUserDO getUserByUsername(String username);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);
}
