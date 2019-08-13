package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.EduException;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduCourseDescription;
import com.guli.teacher.entity.dto.CoursePublishVo;
import com.guli.teacher.entity.query.CourseInfoForm;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-08-02
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //因为需要用到课程描述表  所有注入课程描述接口调用其方法
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节service
    @Autowired
    private EduVideoService eduVideoService;

    //注入章节service
    @Autowired
    private EduChapterService eduChapterService;

    //新增课程信息  课程与描述表
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //因为调用的添加方法 使用到了课程对象 所有创建课程对象 将我们的DTO对象中的关于课程值对拷到课程中 进行添加
        EduCourse eduCourse = new EduCourse();

        eduCourse.setStatus("Draft");

        BeanUtils.copyProperties(courseInfoForm, eduCourse);

        int result = baseMapper.insert(eduCourse);

        if (result <= 0) {
            throw new EduException(20001, "课程添加失败");
        }

        //因为课程描述可以没有也可以有  添加成功与失败都不影响课程的添加 而且是 一对一的关系 所有描述表的id等于课程表的id
        EduCourseDescription courseDescription = new EduCourseDescription();

        //获取描述信息 设置到描述表对象中
        String description = courseInfoForm.getDescription();
        String courseId = eduCourse.getId();

        courseDescription.setDescription(description);
        courseDescription.setId(courseId);

        if (!StringUtils.isEmpty(description)) {
            courseDescriptionService.save(courseDescription);
        }

        return courseId;
    }

    //根据 id 查询课程
    @Override
    public CourseInfoForm getCourseById(String id) {

        EduCourse eduCourse = baseMapper.selectById(id);

        if (eduCourse == null) {
            throw new EduException(20001, "数据不存在");
        }

        CourseInfoForm courseInfoForm = new CourseInfoForm();

        BeanUtils.copyProperties(eduCourse, courseInfoForm);

        EduCourseDescription description = courseDescriptionService.getById(id);

        if (description != null) {
            String d = description.getDescription();

            courseInfoForm.setDescription(d);
        }

        return courseInfoForm;
    }

    //根据 课程id 修改课程信息 设计到两张表 课程 & 描述
    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {

        EduCourse eduCourse = new EduCourse();

        BeanUtils.copyProperties(courseInfoForm, eduCourse);

        int result = baseMapper.updateById(eduCourse);

        if (result <= 0) {
            throw new EduException(20001, "修改课程失败");
        }

        EduCourseDescription courseDescription = new EduCourseDescription();

        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(courseInfoForm.getId());

        courseDescriptionService.updateById(courseDescription);

    }

    //根据课程 id 查询发布课程时的信息数据
    @Override
    public CoursePublishVo getCoursePublishById(String courseId) {

        return baseMapper.getCoursePublishById(courseId);

    }

    //查询所有课程带分页 带条件
    @Override
    public void pageQuery(Page<EduCourse> pageSizeCourse, CourseQuery courseQuery) {

        //因为有条件 所有创建条件查询对象
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //判断传过来的条件是否为空 如果为空则查询分页内容
        if (courseQuery == null) {

            //查询分页内容
            baseMapper.selectPage(pageSizeCourse, wrapper);

            //结束方法
            return;
        }

        //如果条件不为空 则分别获取不同的条件内容进行判断 进行条件查询
        BigDecimal maxPrice = courseQuery.getMaxPrice();
        BigDecimal minPrice = courseQuery.getMinPrice();
        String status = courseQuery.getStatus();
        String title = courseQuery.getTitle();

        //判断获取到的条件是否为空 不为空进行条件拼接
        if (!StringUtils.isEmpty(title)) {

            //进行条件拼接  模糊查询
            wrapper.like("title", title);
        }


        if (!StringUtils.isEmpty(status)) {
            if (status.equals("0")) {
                status = "Draft";

                wrapper.eq("status", status);
            }
        }

        if (!StringUtils.isEmpty(status)) {
            if (status.equals("1")) {
                status = "Normal";

                wrapper.eq("status", status);
            }
        }


        if (!StringUtils.isEmpty(maxPrice)) {
            //查询小于等于该价格的课程
            wrapper.le("price", maxPrice);
        }

        if (!StringUtils.isEmpty(minPrice)) {
            //查询小于等于该价格的课程
            wrapper.ge("price", minPrice);
        }

        baseMapper.selectPage(pageSizeCourse, wrapper);

    }

//    根据课程id 删除课程信息
    @Override
    public void deleteCourseById(String courseId) {

        //1. 根据课程id删除小节
        eduVideoService.deleteVideoByCourseId(courseId);

        //2. 根据课程id删除章节
        eduChapterService.deleteChapterByCourseId(courseId);

        //3. 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //删除封面


        //4. 根据课程id删除课程
        int result = baseMapper.deleteById(courseId);

        if (result <= 0) {
            throw new EduException(20001,"课程删除失败");
        }

    }
}
