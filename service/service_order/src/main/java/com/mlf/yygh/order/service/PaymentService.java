package com.mlf.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mlf.yygh.model.order.OrderInfo;
import com.mlf.yygh.model.order.PaymentInfo;

import java.util.Map;

/**
 * Created by Administrator on 2021/12/8.
 */
public interface PaymentService extends IService<PaymentInfo> {


    //向支付记录表添加信息
    void savePaymentInfo(OrderInfo order, Integer status);

    //更改订单状态
    void paySuccess(String out_trade_no,Map<String, String> resultMap);
}
