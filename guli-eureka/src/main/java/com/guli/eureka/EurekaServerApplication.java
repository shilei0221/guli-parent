package com.guli.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Alei
 * @create 2019-08-06 21:26
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class);
    }
}
