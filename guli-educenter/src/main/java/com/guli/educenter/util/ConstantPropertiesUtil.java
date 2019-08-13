package com.guli.educenter.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alei
 * @create 2019-08-13 11:44
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    //获取配置文件中的微信平台appId
    @Value("${wx.open.app_id}")
    private String appId;

    //获取配置文件中的微信密钥
    @Value("${wx.open.app_secret}")
    private String appSecret;

    //获取配置文件中的要重定向的路径
    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;


    @Override
    public void afterPropertiesSet() throws Exception {

        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
    }
}
