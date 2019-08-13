package com.guli.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Alei
 * @create 2019-07-24 15:51
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.guli.teacher","com.guli.common"})
@EnableEurekaClient
@EnableFeignClients
public class TeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
    }

//    OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL
}