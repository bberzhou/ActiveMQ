package com.bberzhou.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @description: 整合Spring的activemq生产者
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Service
public class SpringMQProducer {

    @Resource
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        // 读取配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 获取对象，添加注解开启包扫描之后，对象名默认是类名小写
        SpringMQProducer springMQProducer = (SpringMQProducer) context.getBean("springMQProducer");
        springMQProducer.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                // TextMessage textMessage = session.createTextMessage("************Spring 和activemq的整合***********");
                TextMessage textMessage = session.createTextMessage("************Spring 和activemq的整合 Listener***********");
                return textMessage;

            }
        });

        // 也可以使用lambda表达式
        // springMQProducer.jmsTemplate.send((session -> {
        //     TextMessage textMessage = session.createTextMessage("************Spring 和activemq的整合***********");
        //     return textMessage;
        // }));
        System.out.println("**************send task over");
    }
}
