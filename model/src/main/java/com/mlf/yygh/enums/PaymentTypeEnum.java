package com.mlf.yygh.enums;

public enum PaymentTypeEnum {
    ALIPAY(1,"支付宝"),
    WEIXIN(2,"微信" );

    private Integer status ;
    private String comment ;

    PaymentTypeEnum(Integer status, String comment ){
        this.status = status;
        this.comment=comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public static void main(String[] args) {
        String statusNameByStatus = PaymentTypeEnum.getStatusNameByStatus(2);
        System.out.println(statusNameByStatus);
    }

    public static String getStatusNameByStatus(Integer status) {
        PaymentTypeEnum[] arrObj = PaymentTypeEnum.values();
        for (PaymentTypeEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getComment();
            }
        }
        return "";
    }

}
