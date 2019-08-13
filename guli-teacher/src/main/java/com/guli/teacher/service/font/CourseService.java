package com.guli.teacher.service.font;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.form.CourseBaseInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Alei
 * @create 2019-08-10 15:09
 *
 * 前台课程模块接口
 */
public interface CourseService extends IService<EduCourse> {
    /**
     *  根据讲师id查询该讲师所讲的课程信息 因为可能有多个所以使用list集合封装
     * @param teacherId
     * @return
     */
    List<EduCourse> getCourseByTeacherId(String teacherId);

    /**
     * 查询课程列表带分页功能
     * @param pageCourse
     * @return
     */
    Map<String,Object> getCourseInfoPage(Page<EduCourse> pageCourse);

    /**
     * 根据课程id查询课程相关信息
     * @param courseId
     * @return
     */
    CourseBaseInfo getBaseCourseInfo(String courseId);
}
