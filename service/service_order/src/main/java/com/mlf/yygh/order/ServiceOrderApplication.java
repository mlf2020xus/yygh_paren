package com.mlf.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2021/12/6.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mlf"})
@EnableDiscoveryClient//将该服务添加到注册中心去
@EnableFeignClients(basePackages = {"com.mlf"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);

    }

}
