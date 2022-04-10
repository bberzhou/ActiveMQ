package com.bberzhou.activemq.produce;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Topic;
import java.util.UUID;

/**
 * @description:
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
public class TopicProducer {
    /**
     *  获取JMS模板
     */
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    /**
     * 获取config中注入的Topic对象
     */
    @Resource
    private Topic topic;

    @Scheduled(fixedDelay = 3000)
    public void produceTopic(){
        jmsMessagingTemplate.convertAndSend(topic,"**** topic msg:"+ UUID.randomUUID().toString().substring(0,6));
        System.out.println("生产成功*********");
    }
}
