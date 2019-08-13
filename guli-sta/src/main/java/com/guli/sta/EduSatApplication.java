package com.guli.sta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Alei
 * @create 2019-08-09 11:58
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.guli.sta","com.guli.common"})
@EnableFeignClients
public class EduSatApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduSatApplication.class,args);
    }
}
