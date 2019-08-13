package com.guli.teacher.service;

import com.guli.teacher.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.form.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
public interface EduVideoService extends IService<EduVideo> {

    //新增小节
    boolean saveVideoInfo(VideoInfoForm videoInfoForm);

    //根据id查询小节信息
    VideoInfoForm getVideoInfoById(String id);

    //修改小节
    boolean updateVideoInfoById(VideoInfoForm videoInfoForm);

    //删除小节
    boolean removeVideoById(String id);

    //1. 根据课程id删除小节
    void deleteVideoByCourseId(String courseId);
}
