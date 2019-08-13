package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.dto.CoursePublishVo;
import com.guli.teacher.entity.query.CourseInfoForm;
import com.guli.teacher.entity.query.CourseQuery;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-08-02
 */
public interface EduCourseService extends IService<EduCourse> {

    //新增课程信息  课程与描述表
    String saveCourseInfo(CourseInfoForm courseInfoForm);

    //根据 id 查询课程
    CourseInfoForm getCourseById(String id);

    //根据 课程id 修改课程信息 设计到两张表 课程 & 描述
    void updateCourseInfo(CourseInfoForm courseInfoForm);

    //根据课程 id 查询发布课程时的信息数据
    CoursePublishVo getCoursePublishById(String courseId);

    //查询所有课程带分页 带条件
    void pageQuery(Page<EduCourse> pageSizeCourse, CourseQuery courseQuery);

//    根据课程id 删除课程信息
    void deleteCourseById(String courseId);
}
