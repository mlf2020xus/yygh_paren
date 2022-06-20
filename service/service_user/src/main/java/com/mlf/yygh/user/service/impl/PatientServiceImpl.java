package com.mlf.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlf.yygh.cmn.client.DictFeignClient;
import com.mlf.yygh.enums.DictEnum;
import com.mlf.yygh.model.user.Patient;
import com.mlf.yygh.user.mapper.PatientMapper;
import com.mlf.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2021/12/3.
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper,Patient> implements PatientService  {

    @Autowired
    DictFeignClient dictFeignClient;

    //获取就诊人列表
    @Override
    public List<Patient> findAllUserId(Long userId) {

    //根据userId查询所有就诊人的信息列表
        QueryWrapper<Patient> wrapper = new QueryWrapper();
        wrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        //这一步直接返回信息不完整，只有编号，需要远程调用数字字典表中的内容
        patientList.stream().forEach(item -> {
            //其他参数的封装
            this.packPatient(item);
        });
        return patientList;

    }

    //根据id获取就诊人的信息
    @Override
    public Patient getPatientId(long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    //其他参数的封装
    public Patient packPatient(Patient patient) {

        //根据证件类型编码，获取证件类型具体值
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //联系人的证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }

}
