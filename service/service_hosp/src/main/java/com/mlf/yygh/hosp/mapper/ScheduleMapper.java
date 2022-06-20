package com.mlf.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlf.yygh.model.hosp.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2021/12/6.
 */
@Mapper
@Repository
public interface ScheduleMapper extends BaseMapper<Schedule> {
}
