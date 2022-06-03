package com.mlf.yygh.msm.controller;

import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.msm.service.MsmService;
import com.mlf.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2021/10/7.
 */
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    //验证码的有效时间的设置
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送手机验证码
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        //从redis获取验证码，如果获取到，返回ok
        // key 手机号  value 验证码   从redis里面获取手机验证码
        String code = redisTemplate.opsForValue().get(phone);
        //如果验证码不等于空，获取成功
        if(!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        //如果从redis获取不到验证码，生成一个随机值，进行阿里云短信发送
        code = RandomUtil.getSixBitRandom();
        //调用service方法，通过整合阿里云短信服务进行发送
        boolean isSend = msmService.send(phone,code);
        //生成验证码放到redis里面，设置有效时间
        if(isSend) {
            //Key，Value,2代表值，TimeUnit.MINUTES代表分钟，表示有效时间2分钟
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("发送短信失败");
        }
    }
}
