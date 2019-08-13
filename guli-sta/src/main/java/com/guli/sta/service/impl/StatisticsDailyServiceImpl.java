package com.guli.sta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.sta.client.EduCenterFeignClient;
import com.guli.sta.entity.StatisticsDaily;
import com.guli.sta.mapper.StatisticsDailyMapper;
import com.guli.sta.service.StatisticsDailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    //注入远程调用的方法
    @Autowired
    private EduCenterFeignClient feignClient;

    //将该天的数据添加到数据库中
    @Override
    public void createSta(String day) {

        //因为我们是根据天数类进行统计的  如果说一天之内多个时间点有不同的数据 我们这样写的代码会出现一天内有很多不同的时间点产生的数据重复
        //就是在数据库中会出现同一天的多条数据  所以我们进行优化   先根据时间进行删除数据  将一天的数据删除  之后再根据日期进行添加，这样每次添加的数据只有一条最新的
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();

        //根据日期条件先进行删除 在进行添加数据 避免出现重复数据
        wrapper.eq("date_calculated",day);

        baseMapper.delete(wrapper);


        //获取统计信息
        //因为我们测试没有那么多数据,所有随机生成一些其他数据 将随机生成的数据设置到统计对象中 用于图表显示
        Integer loginNum = RandomUtils.nextInt(100, 200); //随机生成100-200之间的一个数 做登录人数
        Integer videoViewNum = RandomUtils.nextInt(100,200); //随机生成100-200之间的一个数 做视频播放数量
        Integer courseNum = RandomUtils.nextInt(100,200); //随机生成100-200之间的一个数 做课程数量

        //创建统计信息类
       StatisticsDaily daily = new StatisticsDaily();

       //将其中设置日期
       daily.setDateCalculated(day);

       daily.setLoginNum(loginNum);

       daily.setVideoViewNum(videoViewNum);

       daily.setCourseNum(courseNum);

       //调用远程接口获取每日注册人数
       Integer registerNum = (Integer) feignClient.registerNum(day).getData().get("registerNum");

       //将每日的注册人数设置到统计对象中
       daily.setRegisterNum(registerNum);

        baseMapper.insert(daily);


    }

//    根据条件查询 查询显示对应的数据 封装成数组返回给前端
    @Override
    public Map<String, Object> getChartData(String type, String beginDate, String endDate) {

        //创建条件对象
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();

        //设置条件查询  从开始到结束的日期 区间查询
        wrapper.between("date_calculated",beginDate,endDate);

        //设置查询需要的字段
        wrapper.select(type,"date_calculated");

        //查询出满足条件的集合数据
        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);

        //创建两个集合用来封装我们查询出来的日期数据  与  统计数据
        //因为前台使用的是ECharts图表显示工具,此工具必须传入的数据是数组类型的数据,所以我们后端必须返回数组类型的数据,
        // 前端的数组对应着我们后端的集合,所以创建两个list集合封装数据
        List<String> calculatedList = new CopyOnWriteArrayList<>(new ArrayList<>()); //封装日期数据集合

        List<Integer> dataList = new CopyOnWriteArrayList<>(new ArrayList<>()); //封装统计数据的集合

        //因为返回的是两个数组集合数据  所以我们创建一个map集合用来存放我们封装后的集合数据用于返回前台页面
        Map<String ,Object> map = new ConcurrentHashMap<>(new HashMap<>());

        //遍历该查询出来的数据集合
        for (int i = 0; i < dailyList.size(); i++) {

            //获取每个数据对象
            StatisticsDaily daily = dailyList.get(i);

            //将数据对象中的日期添加到日期集合中
            calculatedList.add(daily.getDateCalculated());

            //因为type的类型是用户选择的 我们不确定 所以我们可以判断是否与前台的值一致，进行数据显示添加，这也是我们为什么将前端的
            //统计因子的值设置成与数据库字段一致的原因
            switch (type) {

                //判断如果是登录数量
                case "login_num":

                    //就添加进数据集合中
                    dataList.add(daily.getLoginNum());

                    break;
                case "register_num":

                    dataList.add(daily.getRegisterNum());

                    break;
                case  "video_view_num" :

                    dataList.add(daily.getVideoViewNum());

                    break;

                case "course_num" :

                    dataList.add(daily.getCourseNum());

                    break;

                default:

                    break;
            }
        }
        //将最终封装的集合数据 放入map集合中
        map.put("calculatedList",calculatedList);

        map.put("dataList",dataList);


        return map;
    }

    public static void main(String[] args) {
        Map<Object, Object> hashMap = new ConcurrentHashMap<>(new HashMap<>());
        hashMap.put("席凯凯",250);
        hashMap.put("席凯",250);
        hashMap.put("凯凯",250);

        Set<Map.Entry<Object, Object>> entries = hashMap.entrySet();

        for (Map.Entry<Object, Object> entry : entries) {
            System.out.println(entry.getKey() + "-->" + entry.getValue());
        }

        Lock lock = new ReentrantLock(true);

    }
}
