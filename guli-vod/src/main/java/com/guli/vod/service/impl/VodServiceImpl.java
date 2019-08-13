package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.guli.common.EduException;
import com.guli.vod.service.VodService;
import com.guli.vod.utils.AliyunVodSDKUtils;
import com.guli.vod.utils.ConstantPropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Alei
 * @create 2019-08-06 16:13
 */
@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {

        try {
            //获取文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名字
            String fileName = file.getOriginalFilename();

            //根据fileName获取title名字 title就是我们上传的文件去掉后缀的名字
            String title = fileName.substring(0,fileName.lastIndexOf("."));

            //创建上传的请求对象 传入参数
            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);
            /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
            //request.setShowWaterMark(true);
            /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
            //request.setCallback("http://callback.sample.com");
            /* 自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData */
            //request.setUserData(""{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}"");
            /* 视频分类ID(可选) */
            //request.setCateId(0);
            /* 视频标签,多个用逗号分隔(可选) */
            //request.setTags("标签1,标签2");
            /* 视频描述(可选) */
            //request.setDescription("视频描述");
            /* 封面图片(可选) */
            //request.setCoverURL("http://cover.sample.com/sample.jpg");
            /* 模板组ID(可选) */
            //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
            /* 工作流ID(可选) */
            //request.setWorkflowId("d4430d07361f0*be1339577859b0177b");
            /* 存储区域(可选) */
            //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
            /* 开启默认上传进度回调 */
            // request.setPrintProgress(true);
            /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
            // request.setProgressListener(new PutObjectProgressListener());
            /* 设置应用ID*/
            //request.setAppId("app-1000000");
            /* 点播服务接入点 */
            //request.setApiRegionId("cn-shanghai");
            /* ECS部署区域*/
            // request.setEcsRegionId("cn-shanghai");

            //创建上传的实现类对象
            UploadVideoImpl uploader = new UploadVideoImpl();

            //创建上传的响应对象 将请求传入
            UploadStreamResponse response = uploader.uploadStream(request);

            //获取上传后的视频id
            String videoId = "";



            if (response.isSuccess()) {

                videoId = response.getVideoId();
                System.out.print("RequestId=" + videoId + "\n");  //请求视频点播服务的请求ID

            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因

                videoId = response.getVideoId();
                System.out.print("RequestId=" + videoId + "\n");  //请求视频点播服务的请求ID

            }

            return videoId;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //根据小节id删除视频
    /**
     * 删除视频
     *  client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    @Override
    public void deleteVideoById(String videoId) {

        try {

            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            //根据视频id删除视频 可以删除多个 可以使用逗号隔开
            request.setVideoIds(videoId);

            DeleteVideoResponse response = client.getAcsResponse(request);

            System.out.print("RequestId删除成功 = " + response.getRequestId() + "\n");

        } catch (Exception e) {
            throw new EduException(20001, "视频删除失败");
        }

    }

    //删除课程时删除多个视频
    @Override
    public void removeVideoList(List<String> videoIdList) {

        try {

            //1.初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //2.创建请求对象 //一次只能批量删除20个
            String str = StringUtils.join(videoIdList.toArray(),",");

            DeleteVideoRequest request = new DeleteVideoRequest();

            request.setVideoIds(str);

            //获取响应
            DeleteVideoResponse response = client.getAcsResponse(request);

            System.out.println(response.getRequestId());

        } catch (Exception e) {
            throw new EduException(20001,"视频删除失败");
        }
    }

    //测试截取字符的方法的使用
    public static void main(String[] args) {
        String name = "woaini.jpg";

        String a = name.substring(0,name.lastIndexOf("."));

        System.out.println(a);
    }
}
