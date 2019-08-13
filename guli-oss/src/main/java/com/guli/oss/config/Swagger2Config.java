package com.guli.oss.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Alei
 * @create 2019-07-24 16:02
 */
@SpringBootConfiguration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();

    }

    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("网站-讲师API文档")
                .description("本文档描述了讲师服务接口定义")
                .version("1.0")
                .contact(new Contact("Alei", "http://alei.com", "541631380@qq.com"))
                .build();
    }
}
