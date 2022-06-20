package com.mlf.yygh.hosp.service;

import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/4.
 */
public interface HospitalService {
    //上传医院的接口
    void save(Map<String, Object> paramMap);
    //根据医院编号查询
    Hospital getByHoscode(String hoscode);
    //根据医院名称查询
    List<Hospital> findByHosName(String hosname);
    //医院列表   page当前页  limit每页的记录数(条件查询带分页)  条件查询用该对象的封装类(vo)
    Page<Hospital> selectHostPage(int page, int limit, HospitalQueryVo hospitalQueryVo);
    //更新医院上线状态
    void updateHospStatus(String id, Integer status);
    //医院详情信息
    Map<String,Object> getHospById(String id);
    //获取医院名称
    String getHospName(String hoscode);
    //根据医院编号获取医院预约挂号详情
    Map<String,Object> item(String hoscode);
}
