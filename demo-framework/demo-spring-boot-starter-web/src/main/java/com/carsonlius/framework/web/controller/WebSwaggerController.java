package com.carsonlius.framework.web.controller;

import com.carsonlius.framework.common.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/28 11:07
 * @company
 * @description
 */
@RestController
@RequestMapping("/web")
@Api(tags = "管理后台 - web module ")
public class WebSwaggerController {

    @GetMapping("/test")
    @ApiOperation(value = "测试分组", notes = "测试swaggger分组")
    public CommonResult<String> test(){
        return CommonResult.success("web模块测试swagger分组配置");
    }
}
