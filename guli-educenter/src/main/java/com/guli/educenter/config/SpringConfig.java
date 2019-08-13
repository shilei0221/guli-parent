package com.guli.educenter.config;


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Alei
 * @create 2019-07-24 16:27
 */
@SpringBootConfiguration
@MapperScan("com.guli.educenter.mapper")
@EnableTransactionManagement
public class SpringConfig {

    //逻辑删除插件
    @Bean
    public ISqlInjector sqlInjector(){ return new LogicSqlInjector(); }

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){return new PaginationInterceptor();}
}
