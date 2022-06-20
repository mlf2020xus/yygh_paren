package com.mlf.yygh.user.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2021/10/7.
 * 这个类主要做读取配置文件，关于微信扫码登陆的相关数据
 *      读取配置文件里面的值
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${wx.open.app_id}")
    private String appId;
    @Value("${wx.open.app_secret}")
    private String appSecret;
    @Value("http://guli.shop/api/ucenter/wx/callback")
    private String redirectUrl;//回调的地址，域名地址
    @Value("http://localhost:3000")
    private String yyghBaseUrl;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;
    public static String YYGH_BASE_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
        YYGH_BASE_URL = yyghBaseUrl;
    }
}
