package com.bberzhou.activemq.produce;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.UUID;

/**
 * @description: 队列生产者
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
public class QueueProducer {

    /**
     *  获取容器中的  jmsMessagingTemplate
     */
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     *  获取Config中注入的  queue，里面配置了queue的destination
     */
    @Resource
    private Queue queue;


    /**
     *  生产者生产消息
     */
    public void produceMsg(){
        // 需要传入destination和 payload
        jmsMessagingTemplate.convertAndSend(queue,"********Springboot active 消息生产完成******"+ UUID.randomUUID().toString().substring(0,6));
    }

    /**
     *  定时生产消息，定时轮询功能，间隔时间3秒定时发送
     *  @Scheduled(fixedDelay = 3000) 注解，表示每3秒钟，调用一次
     */
    @Scheduled(fixedDelay = 3000)
    public void produceMsgScheduled(){
        jmsMessagingTemplate.convertAndSend(queue,"********Springboot active Scheduled 消息生产完成******"+ UUID.randomUUID().toString().substring(0,6));
        System.out.println("Scheduled msg");

    }



}
