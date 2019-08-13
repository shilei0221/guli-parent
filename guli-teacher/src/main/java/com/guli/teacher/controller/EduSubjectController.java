package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.dto.OneSubjectDto;
import com.guli.teacher.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-07-31
 */
@RestController
@RequestMapping("/teacher/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //二级分类添加
    @PostMapping("twoSubject")
    public Result addTwoSubject(@RequestBody EduSubject eduSubject) {

        boolean flag = eduSubjectService.saveTwoSubject(eduSubject);

        if (flag) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //一级分类添加
    @PostMapping("oneSubject")
    public Result addOneSubject(@RequestBody EduSubject eduSubject) {

        boolean flag = eduSubjectService.saveOneSubject(eduSubject);

        if (flag) {
            return Result.ok();
        } else {
            return Result.error().message("保存失败");
        }
    }

    //根据分类id删除分类信息
    @DeleteMapping("{id}")
    public Result deleteSubject(@PathVariable String id) {

        boolean flag = eduSubjectService.removeSubjectById(id);

        if (flag) {

            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //查询一级分类与二级分类的信息
    @ApiOperation(value = "查询所有分类")
    @GetMapping("getAllSubject")
    public Result getAllSubject() {

        //查询所有一级分类与二级分类的方法
        List<OneSubjectDto> list = eduSubjectService.getAllSubjectList();

        return Result.ok().data("items",list);
    }

    //使用 poi 将 Excel 表格导入到数据库中
    @PostMapping("import")
    public Result importData(MultipartFile file) {

        //获取上传文件
        List<String> msg =  eduSubjectService.importDataSubject(file);

        //判断如果返回msg没有数据
        if (msg.size() > 0) { //有提示错误数据

            return Result.error().message("部分数据导入成功").data("msg",msg);
        } else {
            return Result.ok().message("批量数据导入完成");
        }

    }

}

