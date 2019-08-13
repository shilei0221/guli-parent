package com.guli.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.guli.common.Result;
import com.guli.vod.service.VodService;
import com.guli.vod.utils.AliyunVodSDKUtils;
import com.guli.vod.utils.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Alei
 * @create 2019-08-06 16:11
 */
@RestController
@CrossOrigin
@RequestMapping("/eduvod/vod")
@Api(description="阿里云视频点播微服务")
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("uploadVideo")
    public Result uploadVideo(MultipartFile file) {

        String videoId = vodService.uploadVideo(file);

        return Result.ok().data("videoId",videoId);
    }

    @DeleteMapping("{videoId}")
    public Result deleteVideoById(@PathVariable String videoId) {

        //根据小节id删除视频
        vodService.deleteVideoById(videoId);

        return Result.ok();
    }
    /**
     * 批量删除视频
     */
    @DeleteMapping("deleteBatchVideo")
    public Result deleteBatchVideo(@RequestParam("videoIdList")List videoIdList) {

        vodService.removeVideoList(videoIdList);

        return Result.ok();
    }

    /**
     * 根据视频id 获取视频播放凭证 进行播放阿里云视频
     */
    @ApiOperation(value = "根据小节id获取视频播放凭证")
    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(@PathVariable String videoId) {

        try {

            //1.获取阿里云的存储的常量 公钥与密钥
            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;

            //2.进行初始化 将公钥密钥传入
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);

            //3.获取请求与响应对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //将视频id传入request对象中
            request.setVideoId(videoId);

            //4.通过client获取响应数据
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //得到播放凭证
            String playAuth = response.getPlayAuth();

            //5.返回结果
            return Result.ok().message("获取凭证成功").data("playAuth",playAuth);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
