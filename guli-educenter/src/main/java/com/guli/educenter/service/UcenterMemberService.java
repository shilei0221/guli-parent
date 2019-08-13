package com.guli.educenter.service;

import com.guli.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //根据日期查询每日的注册人数
    Integer registerNumCount(String day);

    //通过openId获取用户信息
    UcenterMember getByOpenId(String openId);
}
