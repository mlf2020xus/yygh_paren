package com.mlf.yygh.user.client;

import com.mlf.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator on 2021/12/6.
 */
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {

    //根据就诊人的id获取就诊人信息
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);
}
