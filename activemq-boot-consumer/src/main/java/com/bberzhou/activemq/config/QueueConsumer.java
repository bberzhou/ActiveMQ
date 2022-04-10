package com.bberzhou.activemq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;

/**
 * @description: Springboot queue的消费者
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
public class QueueConsumer {
    /**
     *  获取容器中的  jmsMessagingTemplate
     */
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Value("${myQueue}")
    private String myQueue;

    // /**
    //  *  通过监听的方式，获取消息，
    //  *  这里使用 @JmsListener 注解，并绑定需要监听的destination
    //  * @param textMessage
    //  * @throws JMSException
    //  */
    // @JmsListener(destination = "${myQueue}")
    // public void receive(TextMessage textMessage) throws JMSException {
    //     System.out.println("******boot 消费者收到消息*****"+textMessage.getText());
    // }
    //

    /**
     * 不使用监听器的方式，获取destination中的消息
     * @throws JMSException
     */
    public void receiveMsg() throws JMSException {
        String s = jmsMessagingTemplate.receiveAndConvert(myQueue, String.class);
        System.out.println("******* 消费者收到消息："+s);
    }
}
