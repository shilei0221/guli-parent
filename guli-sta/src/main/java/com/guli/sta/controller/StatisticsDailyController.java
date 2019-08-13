package com.guli.sta.controller;


import com.guli.common.Result;
import com.guli.sta.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Alei
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/sta/daily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    /**
     * 根据日期查询当天的数据 进行添加到统计分析表
     * @param day
     * @return
     */
    @PostMapping("createSta/{day}")
    public Result createSta(@PathVariable String day) {

        //将该天的数据添加到数据库中
        statisticsDailyService.createSta(day);

        return Result.ok();
    }

    /**
     * 根据条件查询 查询显示对应的数据 封装成数组返回给前端
     * @return
     */
    @GetMapping("showChart/{type}/{begin}/{end}")
    public Result showChart(@PathVariable String type,
                            @PathVariable String begin,
                            @PathVariable String end) {

        Map<String,Object> map = statisticsDailyService.getChartData(type,begin,end);

        return Result.ok().data(map);

    }
}

