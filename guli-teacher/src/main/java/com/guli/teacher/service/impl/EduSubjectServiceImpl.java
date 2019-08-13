package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.EduException;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.dto.OneSubjectDto;
import com.guli.teacher.entity.dto.TwoSubjectDto;
import com.guli.teacher.mapper.EduSubjectMapper;
import com.guli.teacher.service.EduSubjectService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-07-31
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //导入表格到数据库
    @Override
    public List<String> importDataSubject(MultipartFile file) {

        //定义一个集合 用来存储错误信息
        List<String> msg = new ArrayList<>();

        try {
            //poi 读取代码
            //1.根据file 获取 文件输入流
            InputStream inputStream = file.getInputStream();

            //2.获取 workbook 对象将文件输入流传入
            Workbook workbook = WorkbookFactory.create(inputStream);

            //3.获取 sheet 表格
            Sheet sheet = workbook.getSheetAt(0);

            //4.获取最后一行的索引 用于遍历多少行 获取数据
           // int physicalNumberOfRows = sheet.getPhysicalNumberOfRows(); //获取物理行数
            int lastRowNum = sheet.getLastRowNum(); //获取最后索引下标


            //5.遍历获取所有的行 列 内容  因为索引从0开始  所有i从1 开始
            for (int i = 1; i <= lastRowNum ; i++) {

                //获取每一行
                Row row = sheet.getRow(i);

                //获取第一列
                Cell cellOne = row.getCell(0);
                //判断是否为空
                if (cellOne == null) {

                    //放入提示信息
                    msg.add("第" + (i+1) +"行的第一列为空~");

                    //如果为空 跳出当前行 下面继续执行
                    continue;
                }

                //获取第一列的内容
                String oneStringCellValue = cellOne.getStringCellValue();
                if (StringUtils.isEmpty(oneStringCellValue)) {

                    msg.add("第" + (i+1) + "行的第一列为空");

                    continue;
                }

                //添加一级分类到数据库中 把 parentId 设置为0
                //创建一个私有的方法 封装判断方法 判断当前表是否存在相同的一级分类
                EduSubject oneEduSubject = this.oneSubject(oneStringCellValue);

                //定义变量存储一级分类id
                String pid = "";

                if (oneEduSubject == null) { //说明没有一级分类 创建一级分类

                    EduSubject eduSubject = new EduSubject();

                    eduSubject.setParentId("0");

                    eduSubject.setTitle(oneStringCellValue);

                    eduSubject.setSort(0);

                    //添加到数据库
                    baseMapper.insert(eduSubject);

                    //获取添加后之后的一级分类id
                    //因为是刚添加进去就去获取,所有在前台管理系统的时候这样做 可能会出现高并发的情况
                    //有一些id可能没有及时获取到   因为我们这是后台管理系统 没有那么多管理员 所以不会出现高并发的情况 所有就可以使用这样简单而直接的方法获取id
                    pid = eduSubject.getId();

                } else {

                    //表有相同的一级分类  获取一级分类id
                    pid = oneEduSubject.getId();
                }

                //获取第二列
                Cell cellTwo = row.getCell(1);
                //判断是否为空
                if (cellTwo == null) {

                    msg.add("第" + (i+1) +"行的第二列为空~");

                    //跳出当前循环  执行下一个
                    continue;
                }

                //获取第二列的值
                String twoStringCellValue = cellTwo.getStringCellValue();
                if (StringUtils.isEmpty(twoStringCellValue)) {

                    msg.add("第" + (i+1) + "行的第一列为空");

                    continue;
                }

                //判断表是否存在相同的二级分类，如果没有添加
                EduSubject twoEduSubject = this.twoSubject(twoStringCellValue, pid);

                if (twoEduSubject == null) { //创建二级分类

                    EduSubject eduSubjectTwo = new EduSubject();

                    eduSubjectTwo.setTitle(twoStringCellValue);

                    eduSubjectTwo.setParentId(pid);

                    eduSubjectTwo.setSort(0);

                    baseMapper.insert(eduSubjectTwo); //添加
                }
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EduException(20001,"系统出异常了");
        }
    }


    //查询所有一级分类与二级分类的方法
    @Override
    public List<OneSubjectDto> getAllSubjectList() {

        //1.查询一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();

        wrapperOne.eq("parent_id","0");

        List<EduSubject> oneEduSubjects = baseMapper.selectList(wrapperOne);

        //2.查询二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();

        wrapperTwo.ne("parent_id","0");

        List<EduSubject> twoEduSubjects = baseMapper.selectList(wrapperTwo);


        //定义一个 list 集合用于封装最终的数据
        List<OneSubjectDto> baseList = new ArrayList<>();

        //3.进行封装
        //遍历一级分类 进行对象赋值
        for (int i = 0; i < oneEduSubjects.size(); i++) {

            //得到每一个一级分类
            EduSubject oneSubject = oneEduSubjects.get(i);

            //使用 BeanUtils工具类提供的对象对拷方法进行对象赋值,将oneSubject对象值赋值给dto对象
            OneSubjectDto oneSubjectDto = new OneSubjectDto();

            BeanUtils.copyProperties(oneSubject,oneSubjectDto);

            //将赋值之后的dto对象添加到最终的集合中
            baseList.add(oneSubjectDto);


            //遍历所有的二级分类
            //先定义一个集合用来存储二级分类的数据对象
            List<TwoSubjectDto> twoSubjectDtoList = new ArrayList<>();

            for (int n = 0; n < twoEduSubjects.size(); n++) {

                //获取每一个二级分类
                EduSubject twoSubject = twoEduSubjects.get(n);

                if (oneSubject.getId().equals(twoSubject.getParentId())) {

                    //将获取到的 二级分类对象 赋值到 dto对象中
                    TwoSubjectDto twoSubjectDto = new TwoSubjectDto();

                    BeanUtils.copyProperties(twoSubject,twoSubjectDto);

                    twoSubjectDtoList.add(twoSubjectDto);
                }
            }
            oneSubjectDto.setChildren(twoSubjectDtoList);
        }
        return baseList;
    }

    //根据分类id删除分类信息
    @Override
    public boolean removeSubjectById(String id) {
        //1 判断分类下面是否有子分类
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("parent_id",id);

        Integer selectCount = baseMapper.selectCount(wrapper);
        //判断
        if (selectCount > 0) { //说明一级分类中有子分类不可以删除

            throw new EduException(20001,"该分类中有子分类,不可以删除");

        } else { //表示该一级分类没有子分类 可以删除
            //删除分类，根据id
            int result = baseMapper.deleteById(id);

            return result > 0;
        }
    }

    //一级分类添加
    @Override
    public boolean saveOneSubject( EduSubject eduSubject) {

        EduSubject existEduSubject = this.oneSubject(eduSubject.getTitle());

        if (existEduSubject == null) {

            eduSubject.setParentId("0");//表示一级分类

            int insert = baseMapper.insert(eduSubject);

            return insert > 0;
        }
        return false;

    }

    //二级分类添加
    @Override
    public boolean saveTwoSubject(EduSubject eduSubject) {

        EduSubject twoSubject = this.twoSubject(eduSubject.getTitle(), eduSubject.getParentId());

        if (twoSubject == null) {

            int insert = baseMapper.insert(eduSubject);

            return insert > 0;
        }
        return false;
    }

    /**
     * 封装：
     *  根据分类名称查询这个一级分类是否存在
     * @param subjectName 一级分类名称
     * @return 返回值
     */
    private EduSubject oneSubject(String subjectName) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("title",subjectName);

        wrapper.eq("parent_id","0");

        return baseMapper.selectOne(wrapper);
    }

    /**
     * 封装：
     *  根据分类名称 与 一级分类id查询二级分类是否存在
     * @param subjectName 二级分类名称
     * @param pid   一级分类id
     * @return 返回值
     */
    private EduSubject twoSubject(String subjectName, String pid) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("title",subjectName);

        wrapper.eq("parent_id",pid);

        return baseMapper.selectOne(wrapper);
    }
}
