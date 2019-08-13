package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.EduException;
import com.guli.teacher.client.VodClient;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.entity.form.VideoInfoForm;
import com.guli.teacher.mapper.EduVideoMapper;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;



    //新增小节
    @Override
    public boolean saveVideoInfo(VideoInfoForm videoInfoForm) {

        //因为要调用小节中的方法 所有创建小节对象 进行对象对拷  将信息对拷进对象进行添加
        EduVideo eduVideo = new EduVideo();

        BeanUtils.copyProperties(videoInfoForm,eduVideo);

        int insert = baseMapper.insert(eduVideo);

        return insert > 0;
    }

    //根据id查询小节信息
    @Override
    public VideoInfoForm getVideoInfoById(String id) {

        EduVideo eduVideo = baseMapper.selectById(id);

        if (eduVideo == null) {
            throw new EduException(20001,"数据不存在");
        }

        //创建videoInfoForm对象进行数据对拷
        VideoInfoForm videoInfoForm = new VideoInfoForm();

        BeanUtils.copyProperties(eduVideo,videoInfoForm);

        return videoInfoForm;
    }

    //修改小节
    @Override
    public boolean updateVideoInfoById(VideoInfoForm videoInfoForm) {

        //创建小节对象进行数据多考 进行修改
        EduVideo eduVideo = new EduVideo();

        BeanUtils.copyProperties(videoInfoForm,eduVideo);

        int update = baseMapper.updateById(eduVideo);

        return update > 0;
    }

    //删除小节
    @Override
    public boolean removeVideoById(String id) {

        //删除视频资源
        //查询云端视频id
        EduVideo eduVideo = baseMapper.selectById(id);

        String videoSourceId = eduVideo.getVideoSourceId();

        //删除视频资源
        if (!StringUtils.isEmpty(videoSourceId)) {

            vodClient.deleteVideoById(videoSourceId);
        }

        Integer delete = baseMapper.deleteById(id);

        return null != delete && delete > 0;
    }

    //1. 根据课程id删除小节
    @Override
    public void deleteVideoByCourseId(String courseId) {


        //1.根据课程id查询所有视频列表
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();

        wrapperVideo.eq("course_id",courseId);

        wrapperVideo.select("video_source_id");

        List<EduVideo> videoList = baseMapper.selectList(wrapperVideo);

        //得到所有视频列表的云端原始视频id
        List<String> videoSourceIdList = new ArrayList<>();

        for (int i = 0; i < videoList.size(); i++) {

            EduVideo eduVideo = videoList.get(i);

            String videoSourceId = eduVideo.getVideoSourceId();

            if (!StringUtils.isEmpty(videoSourceId)) {

                videoSourceIdList.add(videoSourceId);
            }
        }

        //调用vod服务删除远程视频
        if (videoSourceIdList.size() > 0 && videoSourceIdList != null) {

            vodClient.deleteBatchVideo(videoSourceIdList);
        }

        //调用video表示的记录
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();

        wrapper.eq("course_id",courseId);

        baseMapper.delete(wrapper);

    }


}
