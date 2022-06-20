package com.mlf.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.result.ResultCodeEnum;
import com.mlf.yygh.hosp.repository.HospitalRepository;
import com.mlf.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlf.yygh.hosp.mapper.HospitalSetMapper;
import com.mlf.yygh.hosp.service.HospitalSetService;
import com.mlf.yygh.vo.order.SignInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/9/28.
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


    //2、获取表里的签名，然后才能和上面的进行比对。
    //根据传递过来医院编码，查询数据库  查询签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        //查询mysql中的数据，使用mp中的baseMapper
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    //根据医院编号获取医院签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

    @Override
    public List<HospitalSet> list() {
        return null;
    }

    @Override
    public boolean removeById(long id) {
        return false;
    }

    @Override
    public Page<HospitalSet> page(Page<HospitalSet> page, QueryWrapper<HospitalSet> wrapper) {
        return null;
    }

    @Override
    public HospitalSet getById(long id) {
        return null;
    }

    @Override
    public void removeByIds(List<Long> idList) {

    }


    @Override
    public boolean save(HospitalSet entity) {
        return false;
    }

    @Override
    public boolean updateById(HospitalSet entity) {
        return false;
    }


}
