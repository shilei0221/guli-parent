package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Alei
 * @create 2019-08-06 16:13
 */
public interface VodService {


    //上传视频
    String uploadVideo(MultipartFile file);

    //根据小节id删除视频
    void deleteVideoById(String videoId);

    //删除课程时删除多个视频
    void removeVideoList(List<String> videoIdList);
}
