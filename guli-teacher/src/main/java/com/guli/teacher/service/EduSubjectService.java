package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.dto.OneSubjectDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-07-31
 */
public interface EduSubjectService extends IService<EduSubject> {

    //导入表格到数据库
    List<String> importDataSubject(MultipartFile file);


    //查询所有一级分类与二级分类的方法
    List<OneSubjectDto> getAllSubjectList();

    //根据分类id删除分类信息
    boolean removeSubjectById(String id);

    //一级分类添加
    boolean saveOneSubject(EduSubject eduSubject);

    //二级分类添加
    boolean saveTwoSubject(EduSubject eduSubject);
}
