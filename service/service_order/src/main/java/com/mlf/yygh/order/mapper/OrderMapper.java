package com.mlf.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlf.yygh.model.order.OrderInfo;
import com.mlf.yygh.vo.order.OrderCountQueryVo;
import com.mlf.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2021/12/6.
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<OrderInfo> {

    //查询预约统计数据的方法
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
