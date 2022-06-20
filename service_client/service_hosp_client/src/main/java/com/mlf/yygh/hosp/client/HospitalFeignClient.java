package com.mlf.yygh.hosp.client;

import com.mlf.yygh.vo.hosp.ScheduleOrderVo;
import com.mlf.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator on 2021/12/7.
 */
@FeignClient(value = "service-hosp")
@Repository
public interface HospitalFeignClient {

    /** 根据排班id获取预约下单数据 **/
    @GetMapping("/api/hosp/hospital/inner/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId);

    /** 根据医院编号获取医院签名信息 **/
    @GetMapping("/api/hosp/hospital/inner/getSignInfoVo/{hoscode}")
    SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode);
}
