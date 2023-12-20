package com.carsonlius.framework.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/19 17:20
 * @company
 * @description
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class DemoSecurityAutoConfiguration {


    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
    }
}
