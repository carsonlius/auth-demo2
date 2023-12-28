package com.carsonlius.module.system.controller.admin.auth;

import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.carsonlius.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.carsonlius.module.system.service.auth.AdminAuthService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "管理后台 - 认证")
public class AuthController {

    @Resource
    private AdminAuthService authService;

    @ApiOperation("验证token认证是否生效")
    @GetMapping("hello")
    public Object sayHello() {
        return "hello carsonlius!";
    }

    @GetMapping("checkPermission")
    @PermitAll
    @PreAuthorize("@customPermission.hasPermission('system:user:create')")
    @ApiOperation("权限验证")
    public Object sayHello2() {
        return "校验权限通过";
    }

    @PostMapping("/login")
    @PermitAll
    @ApiOperation("使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return CommonResult.success(authService.login(reqVO));
    }
}
