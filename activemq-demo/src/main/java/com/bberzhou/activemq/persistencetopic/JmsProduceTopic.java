package com.bberzhou.activemq.persistencetopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: 测试activeMQ的消息生产者，destination是topic
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsProduceTopic {

    // topic 订阅模式下，要先有生产者发布消息，再启动消费者去消费消息，不然发送的消息就是废消息

    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String TOPIC_NAME = "Topic-Persist";

    public static void main(String[] args) throws JMSException {
        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的生产者
        //  同时设置消息的目的地
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 注意这里做持久化的操作之后再启动，生产的是一个带有持久化的topic
        connection.start();

        // 6、通过使用producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3 ; i++) {
            // 7、创建具体的消息
            // 这里createTextMessage() 方法类似创建一个字符串的消息，类比为学生按照要求写好的问题
            TextMessage textMessage = session.createTextMessage("发送的消息 topic persist msg " + i);

            // 8、通过消息生产者 producer 发送给MQ，还可以在这个send()方法里面传入一些属性
            producer.send(textMessage);

        }

        // 9、关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("**************************topic persist生产者消息发布到MQ完成**************************************");

        /**
         *  注意事项：
         *      如果是topic持久化操作
         *      1、一定要先运行一次消费者，等于向MQ注册，类似我订阅了这个主题(topic)
         *      2、然后再运行生产者发送信息，
         *      3、此时无论消费者是否在线，都会接收到，不在线的话，下次再连接的时候，会把没有受过的消息都接收下来
         */
    }
}
