package com.mlf.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2021/9/28.
 */
@Configuration
@MapperScan("com.mlf.yygh.cmn.mapper")
public class CmnConfig {
    /**
     * 乐观锁插件  mp包下
     */
@Bean
public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
        }

/**
 * 分页插件   mp包下
 */
@Bean
public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
        }
}
