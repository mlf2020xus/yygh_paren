package com.mlf.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mlf.yygh.model.hosp.Hospital;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.vo.user.LoginVo;
import com.mlf.yygh.vo.user.UserAuthVo;
import com.mlf.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

/**
 * Created by Administrator on 2021/10/6.
 */
public interface UserInfoService extends IService<UserInfo>{
    //通过手机号登录
    Map<String,Object> userLoginVo(LoginVo loginVo);
    //判断数据库中是否存在微信扫码人的信息，根据openid做判断
    UserInfo selectWxInfoOpenId(String openId);
    //用户认证接口   (意思就是将之前的信息进行完善)
    void userAuth(Long userId, UserAuthVo userAuthVo);
    //用户列表(条件查询带分页)
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);
    //用户锁定，只需要根据id改变它的状态
    void lock(Long userId, Integer status);
    //用户详情
    Map<String,Object> show(Long userId);
    //认证审批
    void approval(Long userId, Integer authStatus);
}
