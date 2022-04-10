package com.bberzhou.activemq.broker;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.usage.StoreUsage;
import org.apache.activemq.usage.SystemUsage;
import org.apache.activemq.usage.TempUsage;

/**
 * @description: 用ActiveMQ Broker作为独立的消息服务器来构建Java应用
 *              ActiveMQ也支持在VM中通信基于嵌入式的broker，能够无缝的集成其他Java应用
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        // activemq的嵌入式实例broker
        BrokerService brokerService = new BrokerService();
        // 启用broker的JMX监控功能
        brokerService.setUseJmx(true);
        // 这里是绑定程序启动之后的访问地址
        // 修改默认的存储大小, Store limit is 102400 mb
        SystemUsage systemUsage = brokerService.getSystemUsage();
        // Store limit 100MB
        StoreUsage storeUsage = systemUsage.getStoreUsage();
        storeUsage.setLimit(1024*1024*100);
        // Temporary Store limit 100MB
        TempUsage tempUsage = systemUsage.getTempUsage();
        tempUsage.setLimit(1024*1024*100);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
        System.in.read();


        // 启动后自动关闭了，使用activemq-all 5.15.9版本
    }
}
