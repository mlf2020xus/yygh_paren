package com.mlf.yygh.oss.utils;

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
public class ConstantOssPropertiesUtils implements InitializingBean {

    //地域节点  比如在北京
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    //ID
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    //密钥
    @Value("${aliyun.oss.secret}")
    private String secret;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    public static String BUCKET;
    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT=endpoint;
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
        BUCKET=bucket;
    }
}
