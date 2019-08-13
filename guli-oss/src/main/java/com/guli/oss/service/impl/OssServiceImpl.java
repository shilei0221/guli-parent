package com.guli.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.guli.oss.service.OssService;
import com.guli.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author Alei
 * @create 2019-07-30 11:12
 *
 * 实现阿里云OSS上传文件
 */
@Service
public class OssServiceImpl implements OssService {


    @Override
    public String uploadFile(MultipartFile file) {


        try {
            //1.获取一些固定值
            String endPoint = ConstantPropertiesUtil.END_POINT;

            String keyId = ConstantPropertiesUtil.ACCESS_KEY_ID;

            String keySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;

            String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

            //2.获取oss实例对象
            OSSClient ossClient = new OSSClient(endPoint,keyId,keySecret);

            //3.获取文件输入流
            InputStream inputStream = file.getInputStream();

            //4.为上传做准备
            String fileName = file.getOriginalFilename();

            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

            fileName = uuid + fileName;

            String dateTime = new DateTime().toString("yyyy/MM/dd");

            fileName = dateTime + "/" + fileName;


            //5.上传oss
            ossClient.putObject(bucketName,fileName,inputStream);

            //6.关闭oss流
            ossClient.shutdown();

            //7.获取url地址返回
            String urlFile = "https://" + bucketName + "." + endPoint + "/" + fileName;

            return urlFile;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
}
