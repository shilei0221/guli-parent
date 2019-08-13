package com.guli.common;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alei
 * @create 2019-07-25 16:41
 *
 *   统一异常处理类
 *
 *  @ControllerAdvice :
 *      : AOP 面向切面编程 , 不改变源代码的情况下,添加一些功能（增强一些类的功能）就叫 AOP
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error().message("系统出错了");
    }


    //特殊异常处理
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public Result error(BadSqlGrammarException e) {
        e.printStackTrace();
        return Result.error().code(ResultCode.SQL_ERROR).message("SQL语法错误");
    }

    //自定义异常处理
    @ExceptionHandler(EduException.class)
    @ResponseBody
    public Result error(EduException e) {
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }

}
