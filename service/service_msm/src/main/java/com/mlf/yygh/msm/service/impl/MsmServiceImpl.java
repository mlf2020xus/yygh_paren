package com.mlf.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mlf.yygh.msm.service.MsmService;
import com.mlf.yygh.msm.utils.ConstantPropertiesUtils;
import com.mlf.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/7.
 *
 * 整合阿里云短信服务固定模板
 */
@Service
public class MsmServiceImpl implements MsmService{

    //发送手机验证码
    @Override
    public boolean send(String phone, String code) {
        //首先判断手机号是否为空
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        //整合阿里云短信服务
        //设置相关参数
        DefaultProfile profile = DefaultProfile.
                getProfile(ConstantPropertiesUtils.REGION_Id,   //地域id
                        ConstantPropertiesUtils.ACCESS_KEY_ID,  //key
                        ConstantPropertiesUtils.SECRECT);       //value
        IAcsClient client = new DefaultAcsClient(profile);

        //固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);表示是不是HTTPS格式的请求方式
        //表示阿里云的地址固定不能改变
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        Map<String,Object> param = new HashMap();
        param.put("code",code);

        //设置发送相关的参数
        request.putQueryParameter("PhoneNumbers", phone); //手机号
        request.putQueryParameter("SignName", "我的谷粒尚医通项目"); //签名名称  申请阿里云
        request.putQueryParameter("TemplateCode", "SMS_225387943");   //模板code  申请阿里云
        //把map里面的code变成json格式
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    //使用mq发送短信  短信封装
    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getPhone())){
            boolean isSend = this.send(msmVo.getPhone(), msmVo.getParam());
            return isSend;
        }
        return false;
    }
    //mq方法的发送封装
    private boolean send(String phone, Map<String,Object> param) {
        //首先判断手机号是否为空
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        //整合阿里云短信服务
        //设置相关参数
        DefaultProfile profile = DefaultProfile.
                getProfile(ConstantPropertiesUtils.REGION_Id,   //地域id
                        ConstantPropertiesUtils.ACCESS_KEY_ID,  //key
                        ConstantPropertiesUtils.SECRECT);       //value
        IAcsClient client = new DefaultAcsClient(profile);

        //固定模式
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);表示是不是HTTPS格式的请求方式
        //表示阿里云的地址固定不能改变
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //签名名称
        request.putQueryParameter("SignName", "我的谷粒尚医通项目");
        //模板code
        request.putQueryParameter("TemplateCode", "SMS_225387943");
        //把map集合变成json格式
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        //调用方法进行短信发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
