package com.mlf.yygh.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderStatusEnum {
    UNPAID(0,"预约成功，待支付"),
    PAID(1,"已支付" ),
    GET_NUMBER(2,"已取号" ),
    CANCLE(-1,"取消预约"),
    ;

    private Integer status;
    private String comment ;

    OrderStatusEnum(Integer status, String comment ){
        this.status = status;
        this.comment=comment;
    }

    public static void main(String[] args) {
        String statusNameByStatus = OrderStatusEnum.getStatusNameByStatus(1);
        System.out.println(statusNameByStatus);
    }

    public String getComment() {
        return comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static String getStatusNameByStatus(Integer status) {
        OrderStatusEnum[] arrObj = OrderStatusEnum.values();
        for (OrderStatusEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getComment();
            }
        }
        return "";
    }

    //所有订单状态的集合
    public static List<Map<String,Object>> getStatusList() {
        List<Map<String,Object>> list = new ArrayList<>();
        OrderStatusEnum[] arrObj = OrderStatusEnum.values();
        for (OrderStatusEnum obj : arrObj) {
            Map<String,Object> map = new HashMap<>();
            map.put("status", obj.getStatus());
            map.put("comment", obj.getComment());
            list.add(map);
        }
        return list;
    }
}
