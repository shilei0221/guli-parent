package com.guli.educenter.controller;

import com.google.gson.Gson;
import com.guli.educenter.entity.UcenterMember;
import com.guli.educenter.service.UcenterMemberService;
import com.guli.educenter.util.ConstantPropertiesUtil;
import com.guli.educenter.util.HttpClientUtils;
import com.guli.educenter.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author Alei
 * @create 2019-08-13 11:48
 */
@Controller //注意这里没有配置 @RestController   //我们是为了重定向跳转页面 不是为了返回json数据 所以使用 RestController不可以重定向
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     *  根据 文档给定的路径拼接必须的参数最终跳转路径获取二维码
     *
     *  生成登录的二维码
     *  重定向到路径里面 所有返回值写 String 类型
     *
     * @param session
     * @return
     */
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        //1.微信开放平台授权的baseUrl  %s是代表占位符    可以根据相应的值拼接最终的路径
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //2.回调地址  获取业务服务器重定向的地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;

        try {
            //3.进行url编码   请使用urlEncode对链接进行处理  文档中说明
            redirectUrl =  URLEncoder.encode(redirectUrl,"utf-8");


        } catch (Exception e) {

            e.printStackTrace();
        }

        //4.防止 csrf 攻击（跨站请求伪造攻击）    //内网穿透前置域名
        String state = "alei";
        System.out.println("ngrok前置域名为: " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //5.生成 最终的拼接路径
        String baseInfoUrl = String.format(baseUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl, state);


        return "redirect:"+ baseInfoUrl;
    }

    /**
     *  根据 code 临时票据,请求地址（微信提供固定的地址）请求地址之后，获取到两个值
     *
     *  1、access_token 访问凭证
     *  2、 openId 微信唯一标识
     *
     *  1.获取回调参数
     *  2.从redis中读取state进行对比  异常则拒绝调用
     *  3.向微信的授权服务器发起请求  使用临时票据换取 access_token
     *  4.使用上一步获取的openId 查询数据库 判断当前用户是否已经注册 如果已经注册则直接进行登录操作
     *  5.如果未注册  则使用 openId 和 access_token 向微信的资源服务器发起请求 请求获取微信的用户信息
     *      5.1、将获取到的用户信息存入数据库
     *      5.2、 然后进行登录操作
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("callback")
    public String callback(String code, String state) {

        //1.得到授权临时票据code  与内网穿透前置域名
        System.out.println(code);
        System.out.println(state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问
        //我们这里就直接写死了  不存入redis了

        //2.拿着临时票据 code 值  请求腾讯提供的固定的地址
        //向认证服务器发送请求换取两个值  access_token 访问凭证 和 openId 微信唯一标识
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        ////3.拼接地址中的参数  进行拼接参数中的占位符
        baseAccessTokenUrl =  String.format(baseAccessTokenUrl,ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET, code);

        //使用 httpClient 请求拼接之后的地址  获取 AccessToken 凭证和openid 微信唯一标识
        //提供 httpclient 工具类 直接使用就可以了
        String result = "";

        try {

            result = HttpClientUtils.get(baseAccessTokenUrl);
            System.out.println("*****: "  +result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //解析 json 字符串
        //从返回 result 里面获取到 AccessToken 凭证和 openId 微信唯一标识
        //result 是 json 数据格式  使用 json 解析工具 Gson
        Gson gson = new Gson();

        //把 json 数据转换 map 集合
        HashMap hashMap = gson.fromJson(result, HashMap.class);

        //从map集合获取数据
        //access_token 微信访问凭证  openid  微信唯一标识id  这两个值是微信提供的固定的  不可以更改
        String accessToken = (String)hashMap.get("access_token");

        String openId = (String)hashMap.get("openid");

        //查询数据库当前用户是否登录过微信 如果没有进行添加数据库 如果有直接返回结果
        UcenterMember member = memberService.getByOpenId(openId);

        if (null == member) {

            System.out.println("新用户注册");

            //3.拿着获取到的 AccessToken 访问凭证和 openId 微信唯一标识再去访问一个固定地址,获取扫描人的信息
            //访问微信的资源服务器  获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            baseUserInfoUrl = String.format(baseUserInfoUrl,accessToken,openId);

            String userInfo = "";

            try {

                //使用 HTTPClient 请求地址
                userInfo = HttpClientUtils.get(baseUserInfoUrl);
                System.out.println("userInfo:  " + userInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //解析 json     //从返回数据中获取扫描人信息
            HashMap<String,Object> mapUserInfo = gson.fromJson(userInfo,HashMap.class);

            // nickname 微信扫描人名称  headimgurl 微信头像  这两个值都是微信提供的固定的 不可以更改
            String nickName = (String)mapUserInfo.get("nickname");

            String headimgurl = (String)mapUserInfo.get("headimgurl");


            //向数据库中插入一条记录
            member = new UcenterMember();

            member.setNickname(nickName);
            member.setAvatar(headimgurl);
            member.setOpenid(openId);

            //添加到数据库
            memberService.save(member);

        }

        //生成 jwt token值
        String token = JwtUtils.geneJsonWebToken(member);

        //使用url进行传递值
        return "redirect:http://localhost:3000?token=" + token;
    }

}
