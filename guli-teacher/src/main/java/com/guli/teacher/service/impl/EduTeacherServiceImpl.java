package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.mapper.EduTeacherMapper;
import com.guli.teacher.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-07-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    /**
     *  根据多个条件查询讲师列表
     * @param page
     * @param teacherQuery
     */
    @Override
    public void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery) {


        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();


        //判断条件值是否为空，进行条件拼接
        if (teacherQuery == null) {

            baseMapper.selectPage(page,wrapper);

            return ;
        }

        //从该对象中获取条件值
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();


        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }

        if (!StringUtils.isEmpty(begin)) {
            //大于等于开始时间
            wrapper.ge("gmt_create",begin);
        }

        if (!StringUtils.isEmpty(end)) {
            //小于等于结束时间
            wrapper.le("gmt_create",end);
        }

        baseMapper.selectPage(page,wrapper);

    }
}
