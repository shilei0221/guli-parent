package com.guli.oss.controller;

import com.guli.common.Result;
import com.guli.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alei
 * @create 2019-07-30 13:44
 *
 *  阿里云OSS云服务的存储上传文件
 */
@RestController
@RequestMapping("/eduoss/oss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("upload")
    public Result uploadFile(MultipartFile file) {

        String url = ossService.uploadFile(file);

        return Result.ok().data("url",url);
    }
}
