package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.EduException;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.entity.dto.ChapterDto;
import com.guli.teacher.entity.dto.VideoDto;
import com.guli.teacher.mapper.EduChapterMapper;
import com.guli.teacher.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-08-03
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //因为涉及到小节表 所有注入小节的service
    @Autowired
    private EduVideoService eduVideoService;

    //根据课程 id 查询章节&小节信息
    @Override
    public List<ChapterDto> getAllChapterAndVideoByCourseId(String courseId) {

        //1.根据课程id查询章节信息
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();

        wrapperChapter.eq("course_id",courseId);

        List<EduChapter> chapterList = baseMapper.selectList(wrapperChapter);

        //2.根据课程id查询小节信息
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();

        wrapperVideo.eq("course_id",courseId);

        List<EduVideo> videoList = eduVideoService.list(wrapperVideo);

        //定义最终封装数据的list集合
        List<ChapterDto> baseList = new ArrayList<>();

        //3.进行封装对拷DTO
        //遍历章节集合得到每一个章节 进行数据对拷
        for (int i = 0; i < chapterList.size(); i++) {

            //得到每一个章节
            EduChapter eduChapter = chapterList.get(i);

            //创建需要对拷的数据对象
            ChapterDto chapterDto = new ChapterDto();

            //调用方法进行对拷
            BeanUtils.copyProperties(eduChapter,chapterDto);

            //将对拷完的数据添加进最终的集合中
            baseList.add(chapterDto);

            //定义一个新的集合用来存储所有的小节数据
            List<VideoDto> listVideo = new ArrayList<>();
            for (int n = 0; n < videoList.size(); n++) {

                //获取每一个小节对象
                EduVideo eduVideo = videoList.get(n);

                //判断小节中的章节id是否等于章节id值 如果等于的话说明该章节中有小节  在进行数据对拷
                if (eduChapter.getId().equals(eduVideo.getChapterId())) {

                    //创建需要对拷的数据对象
                    VideoDto videoDto = new VideoDto();

                    //调用方法进行对拷
                    BeanUtils.copyProperties(eduVideo,videoDto);

                    //对拷完毕之后添加进集合中
                    listVideo.add(videoDto);
                }
            }
            //将小节对象添加到章节中
            chapterDto.setChildren(listVideo);
        }

        return baseList;
    }

    //根据id删除章节信息 如果章节中有小节则不可以删除
    @Override
    public boolean deleteChapterById(String id) {

        //根据id查询是否存在视频，如果有则提示用户尚有子节点
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();

        wrapper.eq("chapter_id",id);

        int count = eduVideoService.count(wrapper);

        if (count > 0) { //说明有小节 不可以删除
            throw new EduException(20001,"该章节中有小节,不可以删除");
        } else {
            //没有小节 可以删除
            int result = baseMapper.deleteById(id);

            return result > 0;
        }
    }

    //2. 根据课程id删除章节
    @Override
    public void deleteChapterByCourseId(String courseId) {

            QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();

            wrapper.eq("course_id",courseId);

            baseMapper.delete(wrapper);

    }
}
