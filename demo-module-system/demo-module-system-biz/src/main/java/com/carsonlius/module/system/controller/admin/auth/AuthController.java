package com.carsonlius.module.system.controller.admin.auth;

import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.carsonlius.module.system.service.auth.AdminAuthService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

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
    public Object sayHello() {
        return "hello carsonlius!";
    }

    @GetMapping("hello2")
    @PermitAll
    public Object sayHello2() {
        return "hello2 carsonlius!";
    }

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return CommonResult.success(authService.login(reqVO));
    }
}
