package com.guli.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.dto.CoursePublishVo;
import com.guli.teacher.entity.form.CourseBaseInfo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Alei
 * @since 2019-08-02
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    //根据课程 id 查询发布课程时的信息数据
    CoursePublishVo getCoursePublishById(String courseId);

    //根据课程id查询课程相关信息
    CourseBaseInfo getBaseCourseInfo(String courseId);
}
