package com.mlf.yygh.hosp.controller;

import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.hosp.service.ScheduleService;
import com.mlf.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/11/29.
 *
 *
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin//在控制层加这个注解，解决跨域问题    配置了网关的配置类，不需要这个注解了，以免出错
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据医院的编号和科室的编号，查询排班的规则数据
    @ApiOperation(value = "查询排班的规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }
}
