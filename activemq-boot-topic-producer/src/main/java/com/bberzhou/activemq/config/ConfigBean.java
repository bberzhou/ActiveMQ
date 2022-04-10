package com.bberzhou.activemq.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

/**
 * @description: topic的 producer 主配置类
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@Component
public class ConfigBean {
    /**
     *  获取配置文件中的 destination name
     */
    @Value("${myTopic}")
    private String myTopic;

    @Bean
    public Topic topic(){
        // 创建对应名字的Topic
        return new ActiveMQTopic(myTopic);
    }

}
