package com.mlf.yygh.hosp.controller;

import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.helper.HttpRequestHelper;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.hosp.service.HospitalService;
import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/5.
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin//在控制层加这个注解，解决跨域问题    配置了网关的配置类，不需要这个注解了，以免出错
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;


    //医院列表初步实现   page当前页  limit每页的记录数(条件查询带分页)  条件查询用该对象的封装类(vo)
    @GetMapping("list/{page}/{limit}")
    public Result listHost(@PathVariable int page,
                           @PathVariable int limit,
                           HospitalQueryVo hospitalQueryVo){
       Page<Hospital> pageModel = hospitalService.selectHostPage(page,limit,hospitalQueryVo);
        List<Hospital> content = pageModel.getContent();
        long totalElements = pageModel.getTotalElements();
        return Result.ok(pageModel);
    }
    //更新医院上线状态
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id, @PathVariable Integer status){
        hospitalService.updateHospStatus(id,status);
        return Result.ok();
    }
    //医院详情信息
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String,Object> hospital =  hospitalService.getHospById(id);
        return Result.ok(hospital);
    }

    //更新支付状态
    @PostMapping("/order/updatePayStatus")
    public Result updatePayStatus(HttpServletRequest request, HttpServletResponse response){

        try {
            Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//            if (HttpRequestHelper.isSignEquals(paramMap,)){
//
//            }

        }catch (YyghException e){
            return Result.fail().message(e.getMessage());
        }
        return null;
    }
}
