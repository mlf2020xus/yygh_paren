package com.mlf.yygh.hosp.repository;

import com.mlf.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2021/10/5.
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //添加科室
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
