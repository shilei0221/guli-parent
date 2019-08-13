package com.guli.teacher.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alei
 * @create 2019-08-05 16:42
 */
@ApiModel(value = "Course查询对象", description = "课程查询对象封装")
@Data
public class CourseQuery implements Serializable{

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "课程名称,模糊查询")
        private String title;

        @ApiModelProperty(value = "状态 Normal 已发布 Draft 未发布")
        private String status;

        @ApiModelProperty(value = "价格")
        private BigDecimal maxPrice;

        @ApiModelProperty(value = "价格")
        private BigDecimal minPrice;




}
