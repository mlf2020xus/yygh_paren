package com.mlf.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mlf.yygh.cmn.client.DictFeignClient;
import com.mlf.yygh.hosp.repository.HospitalRepository;
import com.mlf.yygh.hosp.service.HospitalService;
import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/4.
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    //远程调用
    @Autowired
    private DictFeignClient dictFeignClient;

    //上传医院的接口将数据传入到mongoDB数据库中
    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换成对象  Hospital
        String mapString = JSONObject.toJSONString(paramMap);//先把map集合转换成字符串，
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);//再把字符串变成对象
        //判断是否存在相同的数据，有修改，
        String hoscode = hospital.getHoscode();
        Hospital hospital1Exist =  hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital1Exist != null){
            hospital.setStatus(hospital1Exist.getStatus());
            hospital.setCreateTime(hospital1Exist.getCreateTime());
            hospital.setUpdateTime(new Date());
            //表示是否删除没有删除，设置为0
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //没有添加操作
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }
    //根据医院编号查询
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    //根据医院名称查询   数据是从MongoDB中的方法
    @Override
    public List<Hospital> findByHosName(String hosname) {

        //findHospitalByHosnameLike  方法名是根据springData的命名规范起的
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //医院列表   page当前页  limit每页的记录数(条件查询带分页)  条件查询用该对象的封装类(vo)
    @Override
    public Page<Hospital> selectHostPage(int page, int limit, HospitalQueryVo hospitalQueryVo) {
        //创建Pageable 对象
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建封装条件的对象，表示模糊查询并忽略字母的大小写
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //将hospitalSetQueryVo对象转换成Hospital对象，传递更加方便
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //sql语句条件封装对象  相当于模糊查询
        Example<Hospital> example = Example.of(hospital,matcher);
        //数据查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        /**从这一步开始远程调用，查询医院等级划分**/
        //需要遍历得到对象查询即可
        //List<Hospital> content = pages.getContent();
        //java8 String流的方式进行遍历  forEach()里面写lamot表达式的形式
        //item表示每次遍历之后得到的Hospital对象
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });
        return pages;
    }
    //更新医院上线状态
    @Override
    public void updateHospStatus(String id, Integer status) {
        //首先根据id获取医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
        //设置修改的值
        hospital.setStatus(hospital.getStatus());
        hospital.setUpdateTime(new Date());
        //数据入库
        hospitalRepository.save(hospital);
    }

    //医院详情信息
    @Override
    public Map<String,Object> getHospById(String id) {
        Map<String,Object> map  = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        //医院的基本信息 (包含医院等级)
        map.put("hospital",hospital);
        //是否预约信息的查询
        map.put("bookingRule",hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return map;
    }

    //获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital != null){
            return hospital.getHosname();
        }
        return null;
    }

    //根据医院编号获取医院预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String,Object> result  = new HashMap<>();
        //医院的基本信息 (包含医院等级)
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital",hospital);
        //预约规则
        result.put("bookingRule",hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取查询list集合，遍历每一个医院，会根据等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictCode和value获取医院等级的名称
        String hospitalString = dictFeignClient.getName("Hospital", hospital.getHostype());
        //查询省、市、地区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

//        hospital.getParam().put("provinceString",provinceString);
//        hospital.getParam().put("cityString",cityString);
//        hospital.getParam().put("districtString",districtString);
        hospital.getParam().put("fullAddress",provinceString + cityString + districtString);
        hospital.getParam().put("hospitalString",hospitalString);
        return hospital;
    }
}
