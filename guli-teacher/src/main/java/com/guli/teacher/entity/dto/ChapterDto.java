package com.guli.teacher.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alei
 * @create 2019-08-03 16:43
 */
@ApiModel(value = "章节信息")
@Data
public class ChapterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;

    private List<VideoDto> children = new ArrayList<>(); //章节里边的小节信息
}
