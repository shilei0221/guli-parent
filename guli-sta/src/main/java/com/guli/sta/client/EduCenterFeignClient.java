package com.guli.sta.client;

import com.guli.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Alei
 * @create 2019-08-09 14:01
 *
 * 远程调用用户表信息的接口
 */

@Component
@FeignClient("guli-educenter")
public interface EduCenterFeignClient {

    @GetMapping("/educenter/member/registerNum/{day}")
    public Result registerNum(@PathVariable("day") String day);
}
