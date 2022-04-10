package com.bberzhou.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 整合Spring的activemq消费者
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Service
public class SpringMQConsumer {
    @Resource
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        // 读取配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 消费者对象，这里 Spring注入之后通过类名首字母小写获取
        SpringMQConsumer springMQConsumer = (SpringMQConsumer)context.getBean("springMQConsumer");

        // 获取mq中的消息
       String  receiveValue = (String)springMQConsumer.jmsTemplate.receiveAndConvert();

        System.out.println("*********************** Spring 消费者收到的消息****:\t"+receiveValue);

    }
}