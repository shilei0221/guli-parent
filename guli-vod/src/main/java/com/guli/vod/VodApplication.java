package com.guli.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Alei
 * @create 2019-08-06 16:11
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class VodApplication {

    public static void main(String[] args) {
        SpringApplication.run(VodApplication.class,args);
    }
}
