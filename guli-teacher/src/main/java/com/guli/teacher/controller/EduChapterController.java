package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.dto.ChapterDto;
import com.guli.teacher.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
@Api(description="课程章节管理")
@RestController
@RequestMapping("/teacher/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //根据课程 id 查询章节&小节信息
    @ApiOperation(value = "章节数据列表")
    @GetMapping("getAllChapterVideo/{courseId}")
    public Result getAllChapterVideo(@PathVariable String courseId) {

        List<ChapterDto> list = eduChapterService.getAllChapterAndVideoByCourseId(courseId);

        return Result.ok().data("items",list);
    }

    //新增章节
    @ApiOperation(value = "新增章节")
    @PostMapping("addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter) {

        boolean save = eduChapterService.save(eduChapter);

        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //修改之前根据id查询章节
    @ApiOperation(value = "根据id查询章节信息")
    @GetMapping("getChapter/{id}")
    public Result getChapter(@PathVariable String id) {

        EduChapter eduChapter = eduChapterService.getById(id);

        return Result.ok().data("chapter",eduChapter);
    }

    //修改章节信息
    @ApiOperation(value = "修改章节信息")
    @PostMapping("updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter) {

        boolean save = eduChapterService.updateById(eduChapter);

        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //删除章节信息
    @ApiOperation(value = "删除章节")
    @DeleteMapping("deleteChapter/{id}")
    public Result deleteChapter(@PathVariable String id) {

        //根据id删除章节信息 如果章节中有小节则不可以删除
        boolean result = eduChapterService.deleteChapterById(id);

        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

}

