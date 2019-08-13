package com.guli.teacher.service;

import com.guli.teacher.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.dto.ChapterDto;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
public interface EduChapterService extends IService<EduChapter> {

    //根据课程 id 查询章节&小节信息
    List<ChapterDto> getAllChapterAndVideoByCourseId(String courseId);

    //根据id删除章节信息 如果章节中有小节则不可以删除
    boolean deleteChapterById(String id);

    //2. 根据课程id删除章节
    void deleteChapterByCourseId(String courseId);
}
