package com.mlf.yygh.hosp.controller.api;
import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.helper.HttpRequestHelper;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.result.ResultCodeEnum;
import com.mlf.yygh.common.utils.MD5;
import com.mlf.yygh.hosp.service.DepartmentService;
import com.mlf.yygh.hosp.service.HospitalService;
import com.mlf.yygh.hosp.service.HospitalSetService;
import com.mlf.yygh.hosp.service.ScheduleService;
import com.mlf.yygh.model.hosp.Department;
import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.model.hosp.Schedule;
import com.mlf.yygh.vo.hosp.DepartmentQueryVo;
import com.mlf.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/4.
 * api  表示对外能够进行调用，外面服务能够调用这个接口
 *  @RequestMapping里面的路径需要注意：这个路径需要与医院模块中的路径匹配，要统一对应
 *     eg:上传医院的接口 http://localhost/api/hosp/saveHospital
 *     eg:上传科室的接口 http://localhost/api/hosp/saveDepartment
 *     eg:上传排班的接口 http://localhost/api/hosp/saveHospital
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    /** 医院 **/
    @Autowired
    private HospitalService hospitalService;

    /** 主要做上传医院的签名校验 **/
    @Autowired
    private HospitalSetService hospitalSetService;
    /** 科室 **/
    @Autowired
    private DepartmentService departmentService;

    /** 排班 **/
    @Autowired
    private ScheduleService scheduleService;

    //删除排班
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
         //参数转成一个map集合
        //获取传递过来的参数数据(排班信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //医院编号 和 排班编号
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        //TODO 签名的校验
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
    //查询排班接口
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //参数转成一个map集合
        //获取传递过来的参数数据(排班信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //医院编号
        String hoscode = (String)paramMap.get("hoscode");
        //科室编号
        String depcode = (String)paramMap.get("depcode");
        //当前页 和 每页的记录数
        int page =  StringUtils.isEmpty(paramMap.get("page")) ? 1 :Integer.parseInt((String)paramMap.get("page"));
        int limit =  StringUtils.isEmpty(paramMap.get("limit")) ? 1 :Integer.parseInt((String)paramMap.get("limit"));
        //TODO 签名的校验

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        //调用service
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }
    //添加排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传递过来的参数数据(排班信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //TODO  签名校验
        //调用service
        scheduleService.save(paramMap);
        return Result.ok();

    }

    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传递过来的参数数据(科室信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //医院编号
        String hoscode = (String)paramMap.get("hoscode");
        //科室编号
        String depcode = (String)paramMap.get("depcode");
        //TODO 签名的校验
        //调用service
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }
    //查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取传递过来的参数数据(科室信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //医院编号
        String hoscode = (String)paramMap.get("hoscode");
        //当前页 和 每页的记录数
       int page =  StringUtils.isEmpty(paramMap.get("page")) ? 1 :Integer.parseInt((String)paramMap.get("page"));
       int limit =  StringUtils.isEmpty(paramMap.get("limit")) ? 1 :Integer.parseInt((String)paramMap.get("limit"));
        //TODO 签名的校验

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }
    //添加科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取传递过来的参数数据(科室信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院编号
        String hoscode = (String)paramMap.get("hoscode");

        /** 签名验证 **/
        //1、获取医院系统传递过来的签名 签名是进行了MD5的加密
        String hospSign = (String)paramMap.get("sign");
        //2、获取表里的签名，然后才能和上面的进行比对。
        //根据传递过来医院编码，查询数据库中的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3、把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);
        //4、判断签名是否一致   传过来的签名和数据库中的签名是否一样
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service里面的方法
        departmentService.save(paramMap);
        return Result.ok();
    }
    //查询医院接口
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        //获取传递过来的参数数据(医院信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院编号
        String hoscode = (String)paramMap.get("hoscode");

        /** 签名校验 **/
        //1、获取医院系统传递过来的签名 签名是进行了MD5的加密
        String hospSign = (String)paramMap.get("sign");

        //2、获取表里的签名，然后才能和上面的进行比对。
        //根据传递过来医院编码，查询数据库中的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3、把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);
        //4、判断签名是否一致   传过来的签名和数据库中的签名是否一样
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service方法实现通过医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //上传医院的接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //获取传递过来的参数数据(医院信息)，最终交到数据库中
        // 在controller中获取post请求来的数据是通过request.getParameterMap()得到
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        /** 签名校验 **/
        //1、获取医院系统传递过来的签名 签名是进行了MD5的加密
        String hospSign = (String)paramMap.get("sign");
        //2、获取表里的签名，然后才能和上面的进行比对。
        //根据传递过来医院编码，查询数据库中的签名
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3、把数据库查询出来的签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);
        //4、判断签名是否一致   传过来的签名和数据库中的签名是否一样
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来，图片base64编码出现的问题
        String logoData = (String)paramMap.get("logoData");//得到图片
        if(!StringUtils.isEmpty(logoData)) {
            logoData = logoData.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }

        //调用service的方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

}
