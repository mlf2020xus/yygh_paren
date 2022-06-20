package com.mlf.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mlf.yygh.hosp.repository.DepartmentRepository;
import com.mlf.yygh.hosp.service.DepartmentService;
import com.mlf.yygh.model.hosp.Department;
import com.mlf.yygh.vo.hosp.DepartmentQueryVo;
import com.mlf.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2021/10/5.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    //添加科室
    @Override
    public void save(Map<String, Object> paramMap) {
        //把map转成字符串
        String paramMapString = JSONObject.toJSONString(paramMap);
        //字符串转换对象
        Department department = JSONObject.parseObject(paramMapString,Department.class);
        //遵循SpringData的规范  根据医院编号 和 科室编号查询。科室可以一样，但医院是唯一的
        //getDepartmentByHoscodeAndDepcode规范命名
        Department departmentExist = departmentRepository.
                getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        //判断有就修改，没有就做添加
        if(departmentExist != null){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }else{
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    //查询科室接口
    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
       //创建Pageable对象，设置当前页和每一页的记录数
        //0是第一页
        Pageable pageable = PageRequest.of(page - 1,limit);
        //创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase(true);
        Example<Department> example = Example.of(department,matcher);

        Page<Department> all = departmentRepository.findAll(example,pageable);
        return all;
    }
    //删除科室接口
    @Override
    public void remove(String hoscode, String depcode) {
        //根据医院编号 和 科室编号查询
        Department department =
                departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        //判断
        if(department != null){
            //调用方法删除
            departmentRepository.deleteById(department.getId());
        }
    }

    //根据医院编号，查询医院所有的科室
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {

        //创建list集合，封装所有的科室信息
        List<DepartmentVo> result = new ArrayList<>();

        //根据医院编号查询所有的科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        //Example对象是对条件的封装
        Example example = Example.of(department);
        //所有科室列表
        List<Department> departmentList = departmentRepository.findAll(example);
        //根据大科室编号 bigcode分组，获取每个大科室里面下级子科室
        Map<String,List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for(Map.Entry<String,List<Department>> entry: departmentMap.entrySet() ){
            //大科室编号
            String bigcode = entry.getKey();
            //大科室里面的所有小科室信息
            List<Department> departmentlist = entry.getValue();

            //封装大的科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(departmentlist.get(0).getBigname());

            //封装小的科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department1 : departmentlist){
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department1.getDepcode());
                departmentVo2.setDepname(department1.getDepname());
                //封装到list集合当中
                children.add(departmentVo2);
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //最终放到result集合里面
            result.add(departmentVo1);
        }
        return result;
    }

    //根据科室编号，和医院编号，查询科室名称
    //根据科室编号，和医院编号，查询科室名称
    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null) {
            return department.getDepname();
        }
        return null;
    }

    //根据科室编号，和医院编号,获取科室信息
    @Override
    public Department getDepartment(String hoscode, String depcode) {

        Department departmentByHoscodeAndDepcode =
                departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return departmentByHoscodeAndDepcode;
    }

}
