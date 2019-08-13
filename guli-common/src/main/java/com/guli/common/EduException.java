package com.guli.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alei
 * @create 2019-07-25 16:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "全局异常")
public class EduException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "异常消息")
    private String msg;

}
