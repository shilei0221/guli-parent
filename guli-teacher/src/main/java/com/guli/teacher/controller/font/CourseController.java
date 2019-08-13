package com.guli.teacher.controller.font;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.dto.ChapterDto;
import com.guli.teacher.entity.form.CourseBaseInfo;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.font.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Alei
 * @create 2019-08-10 15:08
 */
@CrossOrigin
@RestController
@RequestMapping("/teacher/fontCourse")
public class CourseController {


    @Autowired
    private CourseService courseService;

    //注入之前查询章节与小节的义务层接口
    @Autowired
    private EduChapterService eduChapterService;

    /**
     * 查询课程列表带分页功能
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "课程列表展示")
    @GetMapping("getCourseInfo/{page}/{limit}")
    public Result getCourseInfo (@PathVariable Long page,
                                 @PathVariable Long limit) {

        //创建分页对象
        Page<EduCourse> pageCourse = new Page<>(page,limit);

        //调用业务层接口实现功能
        Map<String,Object> map = courseService.getCourseInfoPage(pageCourse);

        return Result.ok().data(map);
    }

    /**
     * 根据课程id查询课程信息 课程描述 所属讲师信息 章节与小节信息
     */
    @ApiOperation(value = "根据id查询课程信息")
    @GetMapping("getBaseCourseInfo/{courseId}")
    public Result getBaseCourseInfo(@PathVariable String courseId) {

        //1.调用之前的章节接口方法根据课程id查询章节与小节信息
        List<ChapterDto> chapterDtoList = eduChapterService.getAllChapterAndVideoByCourseId(courseId);

        //2.调用课程业务层查询课程相关信息 自己写 sql 语句实现
        CourseBaseInfo courseBaseInfo = courseService.getBaseCourseInfo(courseId);

        return Result.ok().data("chapterDtoList",chapterDtoList).data("courseBaseInfo",courseBaseInfo);
    }


}
