package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.form.VideoInfoForm;
import com.guli.teacher.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
@Api(description="小节管理")
@RestController
@RequestMapping("/teacher/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    //添加小节
    @ApiOperation(value = "新增小节")
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody VideoInfoForm videoInfoForm) {
        //新增小节
        boolean flag = eduVideoService.saveVideoInfo(videoInfoForm);

        if (flag) {

            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //修改小节前 根据id查询小节信息
    @ApiOperation(value = "根据id查询小节信息")
    @GetMapping("getVideoById/{id}")
    public Result getVideoById(@PathVariable String id) {
        //根据id查询小节信息
        VideoInfoForm videoInfoForm = eduVideoService.getVideoInfoById(id);

        return Result.ok().data("videoInfoForm",videoInfoForm);

    }

    //修改小节
    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody VideoInfoForm videoInfoForm) {

        //修改小节
        boolean flag = eduVideoService.updateVideoInfoById(videoInfoForm);

        if (flag) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //小节的删除
    @ApiOperation(value = "删除小节")
    @DeleteMapping("deleteVideo/{id}")
    public Result deleteVideo(@PathVariable String id) {
        //删除小节
        boolean result = eduVideoService.removeVideoById(id);

        if (result) {
            return Result.ok();
        } else {
            return Result.error().message("删除失败");
        }
    }

}

