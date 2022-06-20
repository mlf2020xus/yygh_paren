package com.mlf.yygh.task.scheduled;

import com.mlf.common.rabbit.constant.MqConst;
import com.mlf.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2021/12/8.
 */
@Component
@EnableScheduling//表示开启定时任务操作
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    // 每天8点执行 提醒就诊
    //cron表达式,设置执行间隔
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }
}