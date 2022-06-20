package com.mlf.yygh.hosp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mlf.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mlf.yygh.vo.order.SignInfoVo;

import java.util.List;

/**
 * Created by Administrator on 2021/9/28.
 */
public interface HospitalSetService{
    //2、获取表里的签名，然后才能和上面的进行比对。
    //根据传递过来医院编码，查询数据库  查询签名
    String getSignKey(String hoscode);

    //根据医院编号获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);


    List<HospitalSet> list();

    boolean removeById(long id);

    Page<HospitalSet> page(Page<HospitalSet> page, QueryWrapper<HospitalSet> wrapper);

    boolean save(HospitalSet hospitalSet);

    HospitalSet getById(long id);

    boolean updateById(HospitalSet hospitalSet);

    void removeByIds(List<Long> idList);
}
