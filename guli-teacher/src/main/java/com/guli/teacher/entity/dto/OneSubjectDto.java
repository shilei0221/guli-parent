package com.guli.teacher.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alei
 * @create 2019-07-31 16:39
 */
@Data
public class OneSubjectDto {

    private String id; //一级分类id

    private String title; //一级分类名称

    private List<TwoSubjectDto> children = new ArrayList<>(); //一级分类与二级分类的联系 一级分类中有多个二级分类
}
