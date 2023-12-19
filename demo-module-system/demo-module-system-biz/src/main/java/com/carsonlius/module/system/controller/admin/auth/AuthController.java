package com.carsonlius.module.system.controller.admin.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/18 17:58
 * @company
 * @description
 */
@RestController
public class AuthController {

    @GetMapping("hello")
    public Object sayHello(){
        return "hello carsonlius!";
    }
}
