package com.carsonlius.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/18 17:27
 * @company
 * @description
 */
@SpringBootApplication(scanBasePackages = {"com.carsonlius"})
@Slf4j
public class DemoApplication {
    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        Environment env = context.getEnvironment();

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "Doc: \thttp://{}:{}/doc.html\n\t" +
                        "druid: \thttp://{}:{}/druid\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port")
        );
    }
}
