package com.guli.teacher.controller.font;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduTeacherService;
import com.guli.teacher.service.font.CourseService;
import com.guli.teacher.service.font.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Alei
 * @create 2019-08-10 15:03
 *
 * 前台讲师模块的表现层
 */
@CrossOrigin
@RestController
@RequestMapping("/teacher/fontTeacher")
@Api(value = "前台讲师模块")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    //注入teacher业务层 调用其中根据id查询讲师信息方法
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 用于实现前端讲师模块根据 讲师id查询讲师信息 与所讲课程
     */
    @ApiOperation(value = "根据讲师id查询讲师信息")
    @GetMapping("getTeacherInfoById/{id}")
    public Result getTeacherInfoById(@PathVariable String id) {

        //调用讲师接口层根据讲师id查询讲师信息
        EduTeacher eduTeacher = eduTeacherService.getById(id);

        //根据讲师id查询该讲师所讲的课程信息 因为可能有多个所以使用list集合封装
        List<EduCourse> courseList = courseService.getCourseByTeacherId(id);

        //将查询的数据返回 前端接收就是json数据格式
        return Result.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);

    }

    /**
     *  用于实现前台讲师模块的分页查询功能
     *
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("getFontTeacherPage/{page}/{limit}")
    public Result getFontTeacherPage(@PathVariable Long page,
                                     @PathVariable Long limit) {

        //将当前页与当前页显示的数量传入Page对象中
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);

        //调用service层的接口方法 将分页对象传入 因为前台分页需要的参数较多 所以我们将分页对象中的值进行封装入map中
        Map<String,Object> map = teacherService.getFontTeacherPage(pageTeacher);

        //最后将封装后的map分页集合返回给前端
        return Result.ok().data(map);

    }

}
