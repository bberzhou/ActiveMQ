package com.bberzhou.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: 生产者主启动类
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@SpringBootApplication
// 主启动类开启Schedule的功能

@EnableScheduling
public class ProducerMain {
    public static void main(String[] args) {
        SpringApplication.run(ProducerMain.class,args);
    }
}
