package com.carsonlius.module.system.controller.admin.auth;

import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.carsonlius.module.system.service.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/18 17:58
 * @company
 * @description
 */
@RestController
@RequestMapping("/system/auth")
@Slf4j
public class AuthController {

    @Resource
    private AdminAuthService authService;

    @GetMapping("hello")
    public Object sayHello(){
        return "hello carsonlius!";
    }


    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return CommonResult.success(authService.login(reqVO));
    }
}
