package com.mlf.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2021/10/6.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mlf")
@EnableDiscoveryClient//作用：让注册中心发现该服务，扫描该服务
@EnableFeignClients(basePackages = "com.mlf")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
