package com.ruoyi.system.login.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 轮询接口
 *
 * @author zhanghao
 * @date 2023-05-19
 */
@Api(tags = "轮询接口")
@RestController
@RequestMapping("/ping")
public class PingController {

    @Autowired
    private UserService userService;


    @GetMapping
    @ApiOperation("轮询接口")
    public  R ping() {

        HashMap<String, String> map = Maps.newHashMap();
        try {
            userService.getById(3L);
            map.put("db_status", "OK");
            return R.data(map);
        }catch (Exception e){

            map.put("db_status", "FAILD");
            return R.data(map);
        }

    }
}
