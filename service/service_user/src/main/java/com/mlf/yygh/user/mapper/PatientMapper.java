package com.mlf.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlf.yygh.model.user.Patient;
import com.mlf.yygh.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2021/12/3.
 */
@Repository
public interface PatientMapper extends BaseMapper<Patient> {
}
