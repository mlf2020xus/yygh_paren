package com.mlf.yygh.hosp.controller;

import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.hosp.service.DepartmentService;
import com.mlf.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2021/11/29.
 * 医院里面的科室排班信息
 */
@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin//在控制层加这个注解，解决跨域问题    配置了网关的配置类，不需要这个注解了，以免出错
public class DepartmentController {

    @Autowired
   private DepartmentService departmentService;

    //根据医院编号，查询医院所有的科室
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
