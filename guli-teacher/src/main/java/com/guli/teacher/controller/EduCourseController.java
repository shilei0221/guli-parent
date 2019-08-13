package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.dto.CoursePublishVo;
import com.guli.teacher.entity.query.CourseInfoForm;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-08-02
 */
@RestController
@RequestMapping("/teacher/course")
@CrossOrigin
@Api(value = "课程模块")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //新增课程信息  课程与描述表
    @ApiOperation(value = "新增课程")
    @PostMapping("saveLevelSubject")
    public Result oneSubject(@RequestBody CourseInfoForm courseInfoForm) {

        String courseId = courseService.saveCourseInfo(courseInfoForm);

        if (!(StringUtils.isEmpty(courseId))) {
            return Result.ok().data("courseId",courseId);
        } else {
            return Result.error().message("保存失败");
        }

    }

    //根据 id 查询课程
    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("/getCourseById/{id}")
    public Result getCurseById(@PathVariable String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseById(id);
//        ConcurrentHashMap、
        return Result.ok().data("item",courseInfoForm);
    }

    //根据 课程id 修改课程信息 设计到两张表 课程 & 描述
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourse")
    public Result updateCourse(@RequestBody  CourseInfoForm courseInfoForm) {

        courseService.updateCourseInfo(courseInfoForm);

        return Result.ok();
    }

    //根据课程 id 查询发布课程时的信息数据
    @ApiOperation(value = "根据课程id查询课程相关信息")
    @GetMapping("getCoursePublish/{id}")
    public Result getCoursePublishById(@PathVariable String id) {

        CoursePublishVo coursePublishById = courseService.getCoursePublishById(id);

        return Result.ok().data("coursePublish",coursePublishById);

    }

    //根据课程id修改课程状态 为发布课程做准备
    @ApiOperation(value = "根据课程id修改课程状态")
    @PutMapping("updateCourseById/{id}")
    public Result updateCourse(@PathVariable String id) {

        EduCourse course = new EduCourse();

        course.setId(id);
        course.setStatus("Normal");

        courseService.updateById(course);

        return Result.ok();
    }

    //查询所有课程信息 进行列表展示
    @ApiOperation(value = "查询课程信息 进行列表显示")
    @PostMapping("getCourseInfoList/{page}/{limit}")
    public Result getCourseInfoList(@PathVariable Integer page,
                                    @PathVariable Integer limit,
                                    @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                                    @RequestBody CourseQuery courseQuery) {

        //查询所有课程带分页 带条件
        Page<EduCourse> pageSizeCourse = new Page<>(page,limit);

        courseService.pageQuery(pageSizeCourse,courseQuery);

        return Result.ok().data("rows",pageSizeCourse.getRecords()).data("total",pageSizeCourse.getTotal());
    }

    /**
     * 根据课程id 删除课程信息
     */
    @ApiOperation(value = "根据课程id 删除课程信息")
    @DeleteMapping("{courseId}")
    public Result deleteCourseById(@PathVariable String courseId) {

        courseService.deleteCourseById(courseId);

        return Result.ok();

    }
}

