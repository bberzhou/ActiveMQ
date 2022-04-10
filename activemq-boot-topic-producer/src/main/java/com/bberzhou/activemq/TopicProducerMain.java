package com.bberzhou.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: TopicProducer的主启动类
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@SpringBootApplication
@EnableScheduling
public class TopicProducerMain {
    public static void main(String[] args) {
        SpringApplication.run(TopicProducerMain.class,args);
    }
}
