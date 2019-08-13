package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-07-24
 */
@RestController
@RequestMapping("/teacher/teacher")
@CrossOrigin
@Api(value = "讲师模块")

public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;


    //{code: 20000, data: {token: "admin"}}
    @GetMapping("login")
    public Result login() {
        return Result.ok().data("token","admin");
    }
    @GetMapping("info")
    public Result info() {
        return Result.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @PostMapping("logout")
    public Result logout() {
        return Result.ok().data("token","admin");
    }

    @ApiOperation(value = "展示讲师列表")
    @GetMapping("list")
    public Result getTeacherList() {

        List<EduTeacher> teacherList = eduTeacherService.list(null);

        return Result.ok().data("list",teacherList);
    }

    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public Result deleteTeacherById(
            @ApiParam(name = "id" , value = "讲师 ID" ,required = true)
            @PathVariable String id) {

        boolean result = eduTeacherService.removeById(id);

        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    /**
     * @RequestBody 注解 通过json数据格式传递参数，把json格式数据封装到对象里面
     *  必须使用post 请求方式才可以使用该注解
     *  如果使用该注解修饰的值可以为空，则要指定 required 为 false   不是必须的
     *
     * @param page
     * @param limit
     * @param teacherQuery
     * @return
     */
    @ApiOperation(value = "讲师分页方法")
    @PostMapping("getTeacherPageCondition/{page}/{limit}")
    public Result getPageTeacherList(
                                     @ApiParam(name = "page", value = "当前页码", required = true)
                                     @PathVariable Integer page,
                                     @ApiParam(name = "limit", value = "每页记录数", required = true)
                                     @PathVariable Integer limit,
                                     @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
                                     @RequestBody TeacherQuery teacherQuery) {

        Page<EduTeacher> eduTeacherPage = new Page<>(page,limit);

        eduTeacherService.pageQuery(eduTeacherPage ,teacherQuery);

        return Result.ok().data("rows", eduTeacherPage.getRecords()).data("total",eduTeacherPage.getTotal());
    }


    @ApiOperation(value = "讲师新增方法")
    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody EduTeacher eduTeacher) {

        boolean save = eduTeacherService.save(eduTeacher);

        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "根据id查询讲师信息")
    @GetMapping("selectTeacher/{id}")
    public Result selectTeacherById(@PathVariable String id) {

        EduTeacher eduTeacher = eduTeacherService.getById(id);

        return Result.ok().data("teacher",eduTeacher);
    }

    @ApiOperation(value = "修改讲师信息")
    @PutMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher) {
//        @PathVariable String id,
//        eduTeacher.setId(id);

        boolean update = eduTeacherService.updateById(eduTeacher);

        if (update) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

}

