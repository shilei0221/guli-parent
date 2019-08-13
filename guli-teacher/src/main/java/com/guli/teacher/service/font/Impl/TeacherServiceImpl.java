package com.guli.teacher.service.font.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.mapper.EduTeacherMapper;
import com.guli.teacher.service.font.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alei
 * @create 2019-08-10 15:05
 *
 * 前台讲师模块的接口实现
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<EduTeacherMapper,EduTeacher> implements TeacherService {


    /**
     * 用于实现前台讲师模块的分页查询功能
     * @param pageTeacher
     * @return
     */
    @Override
    public Map<String, Object> getFontTeacherPage(Page<EduTeacher> pageTeacher) {

        //根据分页条件查询分页后的对象
        baseMapper.selectPage(pageTeacher,null);

        List<EduTeacher> records = pageTeacher.getRecords();//获取当前页显示的记录数量集合

        long pages = pageTeacher.getPages(); //获取总页数

        long size = pageTeacher.getSize(); //获取当前页的数量

        long total = pageTeacher.getTotal(); //获取总记录数

        long current = pageTeacher.getCurrent(); //获取当前页

        boolean hasPrevious = pageTeacher.hasPrevious(); //是否有上一页

        boolean hasNext = pageTeacher.hasNext(); //是否有下一页

        //定义map集合 用于封装我们获取到的分页数据 最终给前端调用显示
        Map<String ,Object> map = new ConcurrentHashMap<>();

        //将获取到的分页数据加入map中
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
