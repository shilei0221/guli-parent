package com.guli.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Alei
 * @create 2019-07-30 11:10
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
    }
}
