package com.guli.teacher.service.font;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduTeacher;

import java.util.Map;

/**
 * @author Alei
 * @create 2019-08-10 15:04
 *
 * 前台讲师模块接口
 */
public interface TeacherService extends IService<EduTeacher> {


    /**
     * 用于实现前台讲师模块的分页查询功能
     * @param pageTeacher
     * @return
     */
    Map<String,Object> getFontTeacherPage(Page<EduTeacher> pageTeacher);
}
