package com.bberzhou.activemq.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * @description: activemq的配置类
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
// 开启Jms注解，重点

@EnableJms
public class ConfigBean {
    @Value("${myQueue}")
    private String myQueue;

    @Bean
    public Queue queue(){
        // 创建一个名为 myQueue 的 queue，实际的名字写在配置文件中，方便解耦
        return new ActiveMQQueue(myQueue);
    }
}
