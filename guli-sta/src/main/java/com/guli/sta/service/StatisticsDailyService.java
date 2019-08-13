package com.guli.sta.service;

import com.guli.sta.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //将该天的数据添加到数据库中
    void createSta(String day);

//    根据条件查询 查询显示对应的数据 封装成数组返回给前端
    Map<String,Object> getChartData(String type, String beginDate, String endDate);
}
