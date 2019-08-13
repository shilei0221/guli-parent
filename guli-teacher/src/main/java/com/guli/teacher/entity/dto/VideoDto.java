package com.guli.teacher.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Alei
 * @create 2019-08-03 16:40
 */
@ApiModel(value = "小节信息")
@Data
public class VideoDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;

    @ApiModelProperty(value = "云服务器上存储的视频文件名称")
    private String videoOriginalName;
    private String videoSourceId;

}
