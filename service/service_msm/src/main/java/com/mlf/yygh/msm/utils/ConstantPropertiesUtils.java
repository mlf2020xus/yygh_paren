package com.mlf.yygh.msm.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2021/10/7.
 *
 * 这个类主要将阿里云短信服务的验证码的加载。
 *  将其加载到配置文件中，通过这个配置类来读取配置文件里面的值
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${aliyun.msm.regionId}")
    private String regionId;

    @Value("${aliyun.msm.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.msm.secret}")
    private String secret;

    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id=regionId;
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
    }
}
