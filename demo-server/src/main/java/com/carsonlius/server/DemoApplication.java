package com.carsonlius.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/18 17:27
 * @company
 * @description
 */
@SpringBootApplication(scanBasePackages = {"com.carsonlius"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }
}
