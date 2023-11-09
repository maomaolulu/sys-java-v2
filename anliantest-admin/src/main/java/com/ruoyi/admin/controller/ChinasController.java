package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.Chinas;
import com.ruoyi.admin.service.ChinasService;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:06
 */
@RestController
@Api(tags = "省市区三级联动")
@RequestMapping("/liangyuan/chinas")
@CrossOrigin
public class ChinasController {

    @Autowired
    private ChinasService chinasService;


    /**
     * 省市区三级联动
     */
    @GetMapping("/getRegions")
    public R getRegions() {
        List<Chinas> jsonObject = chinasService.getRegions();
        return R.ok("查询成功", jsonObject);
    }

    /**
     * 导入省市区
     * @param chinasList
     * @return
     */
    @PostMapping("/import")
    public R importRegions(@RequestBody List<Chinas> chinasList) {

        Boolean b = chinasService.importRegions(chinasList);

        return R.ok();
    }

//    /**
//     * 导入台湾县区
//     * @return
//     */
//    @GetMapping("/import1")
//    public R import1() {
//
//        Boolean b = chinasService.import1();
//
//        return R.ok();
//    }

}

