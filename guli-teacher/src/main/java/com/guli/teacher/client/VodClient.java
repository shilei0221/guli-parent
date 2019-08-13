package com.guli.teacher.client;

import com.guli.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Alei
 * @create 2019-08-07 10:24
 */
@Component
@FeignClient("guli-vod")
public interface VodClient {

    @DeleteMapping("/eduvod/vod/{videoId}")
    public Result deleteVideoById(@PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/vod/deleteBatchVideo")
    public Result deleteBatchVideo(@RequestParam("videoIdList")List videoIdList);
}
