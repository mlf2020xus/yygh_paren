package com.mlf.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mlf.hospital.mapper.OrderInfoMapper;
import com.mlf.hospital.mapper.ScheduleMapper;
import com.mlf.hospital.model.OrderInfo;
import com.mlf.hospital.model.Patient;
import com.mlf.hospital.model.Schedule;
import com.mlf.hospital.service.HospitalService;
import com.mlf.hospital.util.ResultCodeEnum;
import com.mlf.hospital.util.YyghException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private ScheduleMapper scheduleMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * 预约下单
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> submitOrder(Map<String, Object> paramMap) {
        //表示将Java对象转换json字符串
        log.info(JSONObject.toJSONString(paramMap));

        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        String reserveDate = (String)paramMap.get("reserveDate");
        String reserveTime = (String)paramMap.get("reserveTime");
        String amount = (String)paramMap.get("amount");
        //获取科室信息
        Schedule schedule = this.getSchedule(hosScheduleId);
        if(null == schedule) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }

        if(!schedule.getHoscode().equals(hoscode) || !schedule.getDepcode().equals(depcode)
                || !schedule.getAmount().toString().equals(amount)) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }

        //就诊人信息
        //JSONObject.parseObject（）表示是将Json字符串转化为相应的对象，利用了反射机制
        Patient patient = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Patient.class);
        log.info(JSONObject.toJSONString(patient));
        //处理就诊人业务
        Long patientId = this.savePatient(patient);

        Map<String, Object> resultMap = new HashMap<>();

        //表示剩余预约数量
        int availableNumber = schedule.getAvailableNumber().intValue() - 1;
        if(availableNumber > 0) {
            schedule.setAvailableNumber(availableNumber);
            //数据库入库
            scheduleMapper.updateById(schedule);

            //记录已经预约的数量
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setPatientId(patientId);
            orderInfo.setScheduleId(Long.parseLong(hosScheduleId));
            //总预约数量-剩余预约数量 = 已预约的数量
            int number = schedule.getReservedNumber().intValue() - schedule.getAvailableNumber().intValue();
            //进行修改
            orderInfo.setNumber(number);
            //医事服务费
            orderInfo.setAmount(new BigDecimal(amount));
            String fetchTime = "0".equals(reserveDate) ? " 09:30前" : " 14:00前";
            //取号时间
            orderInfo.setFetchTime(reserveTime + fetchTime);
            //取号地点
            orderInfo.setFetchAddress("一楼9号窗口");
            //默认 未支付
            orderInfo.setOrderStatus(0);
            orderInfoMapper.insert(orderInfo);

            resultMap.put("resultCode","0000");
            resultMap.put("resultMsg","预约成功");
            //预约记录唯一标识（医院预约记录主键）
            resultMap.put("hosRecordId", orderInfo.getId());
            //预约号序
            resultMap.put("number", number);
            //取号时间
            resultMap.put("fetchTime", reserveDate + "09:00前");;
            //取号地址
            resultMap.put("fetchAddress", "一层114窗口");;
            //排班可预约数
            resultMap.put("reservedNumber", schedule.getReservedNumber());
            //排班剩余预约数
            resultMap.put("availableNumber", schedule.getAvailableNumber());
        } else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return resultMap;
    }

    @Override
    public void updatePayStatus(Map<String, Object> paramMap) {
        String hoscode = (String)paramMap.get("hoscode");
        String hosRecordId = (String)paramMap.get("hosRecordId");

        OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if(null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //已支付
        orderInfo.setOrderStatus(1);
        orderInfo.setPayTime(new Date());
        orderInfoMapper.updateById(orderInfo);
    }
    //更新取消预约
    @Override
    public void updateCancelStatus(Map<String, Object> paramMap) {
        String hoscode = (String)paramMap.get("hoscode");
        String hosRecordId = (String)paramMap.get("hosRecordId");

        OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if(null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //已取消
        orderInfo.setOrderStatus(-1);
        orderInfo.setQuitTime(new Date());
        orderInfoMapper.updateById(orderInfo);
    }

    private Schedule getSchedule(String frontSchId) {
        return scheduleMapper.selectById(frontSchId);
    }

    /**
     * 医院处理就诊人信息
     */
    private Long savePatient(Patient patient) {
        // 业务：略
        return 1L;
    }


}
