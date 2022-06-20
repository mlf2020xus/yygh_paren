package com.mlf.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlf.yygh.model.order.OrderInfo;
import com.mlf.yygh.model.order.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2021/12/8.
 */
@Mapper
@Repository
public interface PaymentMapper extends BaseMapper<PaymentInfo> {
}
