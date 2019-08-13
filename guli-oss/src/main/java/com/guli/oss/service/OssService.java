package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alei
 * @create 2019-07-30 11:11
 */
public interface OssService {

    //实现上传方法
    String uploadFile(MultipartFile file);
}
