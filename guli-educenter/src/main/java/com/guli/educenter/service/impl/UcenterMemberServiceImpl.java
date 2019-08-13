package com.guli.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.educenter.entity.UcenterMember;
import com.guli.educenter.mapper.UcenterMemberMapper;
import com.guli.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    //根据日期查询每日的注册人数
    @Override
    public Integer registerNumCount(String day) {

        return baseMapper.registerNumCount(day);
    }

    //通过微信id获取用户信息
    @Override
    public UcenterMember getByOpenId(String openId) {

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();

        wrapper.eq("openid",openId);

        UcenterMember member = baseMapper.selectOne(wrapper);

        return member;
    }


}
