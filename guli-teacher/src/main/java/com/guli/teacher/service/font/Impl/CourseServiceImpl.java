package com.guli.teacher.service.font.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.form.CourseBaseInfo;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.font.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alei
 * @create 2019-08-10 15:10
 *
 * 前台课程模块接口的实现
 */
@Service
public class CourseServiceImpl extends ServiceImpl<EduCourseMapper,EduCourse> implements CourseService {


    /**
     *  根据讲师id查询该讲师所讲的课程信息 因为可能有多个所以使用list集合封装
     * @param teacherId
     * @return
     */
    @Override
    public List<EduCourse> getCourseByTeacherId(String teacherId) {

        //创建查询条件对象
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //根据讲师id查询课程信息
        wrapper.eq("teacher_id",teacherId);

        //根据创建时间进行倒叙排序
        wrapper.orderByDesc("gmt_modified");

        //查询课程列表集合
        List<EduCourse> courseList = baseMapper.selectList(wrapper);

        return courseList;
    }

    /**
     * 查询课程列表带分页功能
     * @param pageCourse
     * @return
     */
    @Override
    public Map<String, Object> getCourseInfoPage(Page<EduCourse> pageCourse) {

        //根据创建时候进行倒序
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");

        //查询课程列表分页集合数据
        baseMapper.selectPage(pageCourse,queryWrapper);

        List<EduCourse> records = pageCourse.getRecords(); //当前页数据集合

        long current = pageCourse.getCurrent(); //当前页

        long total = pageCourse.getTotal(); //总记录数

        long size = pageCourse.getSize(); //当前页的数量

        long pages = pageCourse.getPages(); //总页数

        boolean hasNext = pageCourse.hasNext(); //是否有下一页

        boolean hasPrevious = pageCourse.hasPrevious(); //是否有上一页

        //创建map集合用来封装我门查询出来的分页数据
        Map<String,Object> map = new ConcurrentHashMap<>();

        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    /**
     * 根据课程id查询课程相关信息
     * @param courseId
     * @return
     */
    @Override
    public CourseBaseInfo getBaseCourseInfo(String courseId) {

        return baseMapper.getBaseCourseInfo(courseId);
    }
}
