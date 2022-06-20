package com.mlf.hospital.mapper;

import com.mlf.hospital.model.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

}
