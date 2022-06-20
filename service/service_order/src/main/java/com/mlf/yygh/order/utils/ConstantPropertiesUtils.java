package com.mlf.yygh.order.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2021/12/8.
 *
 * 读取配置文件里的微信支付数据类
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean{

    @Value("${weixin.pay.appid}")
    private String appid;   //关联的公众号 wx74862e0dfcf69954

    @Value("${weixin.pay.partner}")
    private String partner; //商户号  1558950191

    @Value("${weixin.pay.partnerkey}")
    private String partnerkey;  //商户KEY T6m9iK73b0kn9g5v426MKfHQH7X8rKwb

    public static String APPID;   //公众号
    public static String PARTNER;  //商户号
    public static String PARTNERKEY;  //商户key

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appid;
        PARTNER = partner;
        PARTNERKEY = partnerkey;
    }
}
