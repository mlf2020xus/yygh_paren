package com.mlf.common.rabbit.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2021/12/7.
 */

@Service
public class RabbitService {

    //spring的用来发送消息的类，spring boot会将RabbitTemplate自动注入到容器中
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     *  发送消息的方法
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息的内容
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

    /**
     * 在测试类中编写方法进行测试：
     @SpringBootTest
     class SpringRabbitmqApplicationTests {

     @Autowired
     private RabbitTemplate rabbitTemplate;
     @Test
     void contextLoads() {
             rabbitTemplate.convertAndSend("exchange.direct","leslie",new User("leslie","1234567"));
        }
     }
     rabbitTemplate的convertAndSend()方法可以给指定队列发送消息，函数有三个参数，
     第一个是**交换机(exchange)的名字,
     第二个是路由键(routing-key)**的名字，
     第三个则为消息的内容。
     我们这里给名叫exchange.direct的交换机发送一个User对象，
     其中路由键为leslie,因为交换机类型是direct，所以是点对点的消息队列，
     只会发送到名叫leslie的队列中，
     运行程序，去web管理界面查看是否发送成功:
     */
}
