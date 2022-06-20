package com.mlf.yygh.user.utils;

import io.swagger.models.auth.In;

/**
 * Created by Administrator on 2021/11/15.
 */
public enum PaymentTypeEnum {

    ALIPAY(1,"支付宝"),
    WEIXIN(2,"微信");

    private Integer status;
    private String comment;

    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

    PaymentTypeEnum(Integer status, String comment){
        this.status = status;
        this.comment = comment;
    }

    public Integer test1(){
        Integer status = PaymentTypeEnum.WEIXIN.getStatus();
        return status;
    }

}
