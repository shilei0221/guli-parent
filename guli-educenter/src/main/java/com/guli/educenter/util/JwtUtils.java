package com.guli.educenter.util;

/**
 * @author Alei
 * @create 2019-08-13 19:01
 *
 * JWT工具类
 */
import com.guli.educenter.entity.UcenterMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {


    public static final String SUBJECT = "Alei";

    //秘钥
    public static final String APPSECRET = "Alei";

    public static final long EXPIRE = 1000 * 60 * 30;  //过期时间，毫秒，30分钟


    /**
     * 生成jwt token 字符串
     *
     * @param member
     * @return
     */
    public static String geneJsonWebToken(UcenterMember member) {

        //如果member对象为空 返回空
        if (member == null || StringUtils.isEmpty(member.getId())
                || StringUtils.isEmpty(member.getNickname())
                || StringUtils.isEmpty(member.getAvatar())) {
            return null;
        }

        //如果member对象不为空 构建数据 设置分类
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", member.getId())    //设置主体信息 用户信息
                .claim("nickname", member.getNickname())
                .claim("avatar", member.getAvatar())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   //设置过期时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact(); //进行加密

        return token; //返回生成的token字符串
    }


    /**
     * 校验jwt token
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
        return claims;
    }

    //测试生成jwt token
    private static String testGeneJwt(){
        UcenterMember member = new UcenterMember();
        member.setId("999");
        member.setAvatar("www.guli.com");
        member.setNickname("Helen");

        String token = JwtUtils.geneJsonWebToken(member);
        System.out.println(token);
        return token;
    }

    //测试校验jwt token
    private static void testCheck(String token){

        Claims claims = JwtUtils.checkJWT(token);
        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");
        String id = (String)claims.get("id");
        System.out.println(nickname);
        System.out.println(avatar);
        System.out.println(id);
    }


    public static void main(String[] args){
        String token = testGeneJwt();
        testCheck(token);
    }
}
