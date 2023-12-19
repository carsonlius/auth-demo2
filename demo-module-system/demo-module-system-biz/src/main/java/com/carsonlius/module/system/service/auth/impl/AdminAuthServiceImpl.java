package com.carsonlius.module.system.service.auth.impl;

import com.carsonlius.framework.common.enums.CommonStatusEnum;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.carsonlius.module.system.dal.dataobject.user.AdminUserDO;
import com.carsonlius.module.system.enums.ErrorCodeConstants;
import com.carsonlius.module.system.service.auth.AdminAuthService;
import com.carsonlius.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 10:00
 * @company
 * @description
 */
@Service
@Slf4j
public class AdminAuthServiceImpl implements AdminAuthService {

    @Resource
    private AdminUserService userService;

    @Override
    public AdminUserDO authenticate(String username, String password) {

        //  检查账号是否存在
        AdminUserDO user = userService.getUserByUsername(username);
        if (user == null) {
            // todo 记录登录日志
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }

        //  检查密码是否匹配
        if (!userService.isPasswordMatch(password, user.getPassword())) {

            // todo 记录登录日志

            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }

        //  检查账号状态
        if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
            // todo 记录日志
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
        }

        return user;
    }


    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        // todo 校验验证码

        // 账号密码登录
        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());

        // todo 如果 socialType 非空，说明需要绑定社交用户

        // todo 创建token, 记录日志



        return null;
    }

    @Override
    public void logout(String token, Integer logType) {

    }
}
