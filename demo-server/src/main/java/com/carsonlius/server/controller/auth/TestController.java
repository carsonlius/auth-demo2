package com.carsonlius.server.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/18 17:29
 * @company
 * @description
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public Object test(){
        return "hello world";
    }
}
