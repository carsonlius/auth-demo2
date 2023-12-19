package com.carsonlius.module.system.service.user.impl;

import com.carsonlius.module.system.dal.dataobject.user.AdminUserDO;
import com.carsonlius.module.system.dal.mysql.user.AdminUserMapper;
import com.carsonlius.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 10:34
 * @company
 * @description
 */

@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper userMapper;

//    @Resource
//    private PasswordEncoder passwordEncoder;

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
//        return passwordEncoder.matches(rawPassword, encodedPassword);
        return true;
    }
}
