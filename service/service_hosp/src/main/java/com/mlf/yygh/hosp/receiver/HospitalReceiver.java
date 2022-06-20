package com.mlf.yygh.hosp.receiver;

//import com.mlf.common.rabbit.constant.MqConst;
//import com.mlf.common.rabbit.service.RabbitService;
import com.mlf.common.rabbit.constant.MqConst;
import com.mlf.common.rabbit.service.RabbitService;
import com.mlf.yygh.hosp.service.ScheduleService;
import com.mlf.yygh.model.hosp.Schedule;
import com.mlf.yygh.vo.msm.MsmVo;
import com.mlf.yygh.vo.order.OrderMqVo;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2021/12/7.
 */
@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    //监听是否有内容
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
//            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
//            key = {MqConst.ROUTING_ORDER}
//    ))
//    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
//        //下单成功更新预约数
//        Schedule schedule = scheduleService.getScheduleId(orderMqVo.getScheduleId());
//        schedule.setReservedNumber(orderMqVo.getReservedNumber());
//        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
//        scheduleService.update(schedule);
//        //发送短信
//        MsmVo msmVo = orderMqVo.getMsmVo();
//        if(null != msmVo) {
//            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
//        }
//    }
}
