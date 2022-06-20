package com.mlf.yygh.order.service;

import java.util.Map;

/**
 * Created by Administrator on 2021/12/8.
 */
public interface WeixinService {

    //下单 生成微信支付二维码
    Map createNative(Long orderId);


    //调用微信接口实现支付状态查询
    Map<String,String> queryPayStatus(Long orderId, String name);
}
