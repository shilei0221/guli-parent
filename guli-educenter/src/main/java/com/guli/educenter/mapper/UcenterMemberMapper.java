package com.guli.educenter.mapper;

import com.guli.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //根据日期查询每日的注册人数
    Integer registerNumCount(String day);
}
