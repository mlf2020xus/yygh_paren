package com.mlf.yygh.hosp.controller;

import com.mlf.yygh.model.hosp.HospitalSet;
import com.mlf.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.utils.MD5;
import com.mlf.yygh.hosp.service.HospitalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2021/9/28.
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin//在控制层加这个注解，解决跨域问题    配置了网关的配置类，不需要这个注解了，以免出错
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //http://localhost:8201/admin/hosp/hospitalSet/findAll
    //1、查询医院设置表的所有信息
    @ApiOperation(value = "获取所有的医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        Result<List<HospitalSet>> ok = Result.ok(list);
        return ok;
    }
    //2、逻辑删除医院设置表
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable() long id){
        boolean flag = hospitalSetService.removeById(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    //3、条件查询带分页  第三个代表条件对象
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current")long current,
                                  @PathVariable("limit")long limit,
                                 @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        //创建page对象，传递当前页，每页的记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        //上面的第三个参数对象(条件对象)  用到mp中的QueryWrapper(条件构造器)
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        //调用方法  实现分页查询
        Page<HospitalSet> page1HospitalSet = hospitalSetService.page(page, wrapper);
        //返回结果
        return Result.ok(page1HospitalSet);
    }
    //4 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1 可以使用  0 不能使用
            hospitalSet.setStatus(1);

            //签名秘钥(作用就是能与医院接口对接成功)
            Random random = new Random();
            hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

            //调用service添加
            boolean save = hospitalSetService.save(hospitalSet);
            if(save){
                return Result.ok();
            }else{
                return Result.fail();
            }

    }

    //5 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable long id){

        try {
        }catch (Exception e){
            throw new YyghException("失败了",201);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }
    //6 修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
            if(flag){
                return Result.ok();
            }else{
                return Result.fail();
            }
    }
    //7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }
    //8 医院设置锁定和解锁(@PutMapping()修改操作)
    // (解锁状态才能实现与医院接口对接，实现数据的操作，锁定就不能操作就是设置status的值 1能使用 0 奴能使用)
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        //先根据Id 查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return  Result.ok();
    }
    //9 发送签名密钥 （医院设置接口与平台对接的一个密钥  发短信验证码
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();//签名密钥
        String hoscode = hospitalSet.getHoscode();//医院编号
        //TODO 发送短信

        return Result.ok();

    }

}
