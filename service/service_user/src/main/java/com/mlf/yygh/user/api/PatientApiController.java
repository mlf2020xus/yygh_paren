package com.mlf.yygh.user.api;

import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.utlis.AuthContextHolder;
import com.mlf.yygh.model.user.Patient;
import com.mlf.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2021/12/3.
 *
 * 就诊人管理接口
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    PatientService patientService;

    //获取就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request){
        //获取当前用户id
        Long userId = AuthContextHolder.getUserId(request);
        //获取就诊人列表，将其信息返回
        List<Patient> patientList = patientService.findAllUserId(userId);
        return Result.ok(patientList);
    }
    //添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request){
        //获取当前用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //根据id获取就诊人的信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable long id){
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
    //修改就诊人信息
    //@PostMapping
    @PutMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.ok();
    }
    //删除就诊人信息
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable long id){
        patientService.removeById(id);
        return Result.ok();
    }

    //根据就诊人的id获取就诊人信息
    @GetMapping("inner/get/{id}")
    public Result getPatientOrder(@PathVariable Long id){
       Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
}
