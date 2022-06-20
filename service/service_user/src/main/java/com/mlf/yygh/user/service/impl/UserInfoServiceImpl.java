package com.mlf.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.helper.JwtHelper;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.result.ResultCodeEnum;
import com.mlf.yygh.enums.AuthStatusEnum;
import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.model.user.Patient;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.user.mapper.UserInfoMapper;
import com.mlf.yygh.user.service.PatientService;
import com.mlf.yygh.user.service.UserInfoService;
import com.mlf.yygh.vo.user.LoginVo;
import com.mlf.yygh.vo.user.UserAuthVo;
import com.mlf.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/6.
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    //从redis里面获取手机验证码
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //就诊人得相关数据
    @Autowired
    private PatientService patientService;


    //通过手机号登录
    @Override
    public Map<String, Object> userLoginVo(LoginVo loginVo) {
        //1、从loginVo获取输入的手机号 和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //2、判断手机号 和验证码是否为空
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //3、TOD判断手机验证码和输入的验证码是否一致   需要整合阿里云的短信息服务,先从redis里面获取手机验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if(!code.equals(redisCode)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码
        UserInfo userInfo = null;
        //若果Openid不等于空，查询数据库，进行手机号绑定
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
        //如果userInfo为空，则进行正常的手机登录
        if(userInfo == null){
            //4、判断是否第一次登录：根据手机号查询数据库，如果不存在相同手机号就是第一次登录
            //通过QueryWrapper来封装数据库中的字段
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phone);
            userInfo = baseMapper.selectOne(wrapper);
            if(userInfo == null){//第一个使用这个手机号登录
                //添加手机号到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }

        //5、不是第一次登录，数据库中存在相同的手机号，就直接登录
        //校验是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        /** 6、返回登录信息 返回登陆用户名  返回token信息 **/
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //利用jwt token的生成   会利用JWT
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    //判断数据库中是否存在微信扫码人的信息，根据openid做判断
    @Override
    public UserInfo selectWxInfoOpenId(String openId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openId",openId);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
       return userInfo;
    }

    //用户认证接口  (进一步完善信息的过程)
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //获取用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        //设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行数据更新
        baseMapper.updateById(userInfo);
    }


    //用户列表(条件查询带分页)
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword();//用户名称
        Integer status = userInfoQueryVo.getStatus();//用户得状态
        Integer authStatus = userInfoQueryVo.getAuthStatus();//得到用户认证得状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();//开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();//结束时间
        //对条件值进行非空的判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)){
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }
        //调用mapper
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //根据字段上的编号，找到对应的值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

    //用户锁定，只需要根据id改变它的状态
    @Override
    public void lock(Long userId, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    //用户详情
    @Override
    public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
        //根据userId查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        //根据userId查询就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList",patientList);
        return map;
    }

    //认证审批 -1审核不通过、2审核通过
    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue() == 2 || authStatus.intValue() == -1){
            //返回值是这个对象  UserInfo
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    //根据字段上的编号，找到对应的值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus().intValue()==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }
}

