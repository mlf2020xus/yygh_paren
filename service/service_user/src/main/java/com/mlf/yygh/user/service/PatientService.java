package com.mlf.yygh.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mlf.yygh.model.user.Patient;
import com.mlf.yygh.model.user.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2021/12/3.
 */
public interface PatientService extends IService<Patient> {

    //获取就诊人列表
    List<Patient> findAllUserId(Long userId);

    //根据id获取就诊人的信息
    Patient getPatientId(long id);
}
