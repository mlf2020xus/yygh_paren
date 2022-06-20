package com.mlf.yygh.hosp.repository;

import com.mlf.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2021/10/4.
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);

    //根据医院名称查询   数据是从MongoDB中的方法
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
