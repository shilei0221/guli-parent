package com.guli.educenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Alei
 * @create 2019-08-09 11:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.guli.educenter","com.guli.common"})
@EnableEurekaClient
public class EducenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducenterApplication.class,args);
    }
}
