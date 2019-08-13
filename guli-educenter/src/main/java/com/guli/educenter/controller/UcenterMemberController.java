package com.guli.educenter.controller;


import com.guli.common.Result;
import com.guli.educenter.entity.UcenterMember;
import com.guli.educenter.service.UcenterMemberService;
import com.guli.educenter.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @GetMapping("registerNum/{day}")
    @ApiOperation(value = "每日注册人数")
    public Result registerNum(@PathVariable String day) {

        //根据日期查询每日的注册人数
        Integer num = ucenterMemberService.registerNumCount(day);

        return Result.ok().data("registerNum",num);
    }


    /**
     *  显示用户信息
     */
    @PostMapping("info/{token}")
    public Result info(@PathVariable String token) {

        //根据 jwt 工具类中的方法传入token 字符串 获取用户信息
        Claims claims = JwtUtils.checkJWT(token);

        //获取用户信息
        String nickName = (String)claims.get("nickname");

        String avatar = (String)claims.get("avatar");

        String id = (String)claims.get("id");

        UcenterMember member = new UcenterMember();

        //将用户信息设置到对象中一起返回
        member.setId(id);
        member.setNickname(nickName);
        member.setAvatar(avatar);

        return Result.ok().data("memberInfo",member);
    }
}

