package com.mlf.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2021/9/28.
 */
//@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@SpringBootApplication
@ComponentScan(basePackages = "com.mlf")//指向swagger所在包
@EnableDiscoveryClient//表示将这个模块添加到注册中心nacos上，需要进一步实现远程调用
@EnableFeignClients(basePackages = "com.mlf")//表示根据@FeignClient("service-cmn")来找到这个模块的调用
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
