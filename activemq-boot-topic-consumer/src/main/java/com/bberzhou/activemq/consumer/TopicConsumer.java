package com.bberzhou.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @description: topic的consumer
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
public class TopicConsumer {

    @JmsListener(destination = "${myTopic}")
    public void receiveTopic(TextMessage textMessage) {
        try {

            System.out.println("*****消费者收到订阅的主题：" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
