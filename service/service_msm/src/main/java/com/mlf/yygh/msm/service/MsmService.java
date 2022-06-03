package com.mlf.yygh.msm.service;

import com.mlf.yygh.vo.msm.MsmVo;

/**
 * Created by Administrator on 2021/10/7.
 */
public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);

    //使用mq发送短信
    boolean send(MsmVo msmVo);
}
