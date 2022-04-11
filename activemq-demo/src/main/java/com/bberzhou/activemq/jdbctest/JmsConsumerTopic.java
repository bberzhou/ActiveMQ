package com.bberzhou.activemq.jdbctest;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试MQ的消息消费者，destination是topic，测试持久化存储
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerTopic {
    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String TOPIC_NAME = "topic_jdbc";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号topic消息订阅者");

        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        // 2.1 在连接上设置消费者的id，用来识别消费者
        connection.setClientID("topic01");

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的消费者，需要找相同的destination，queue或者topic,创建订阅者
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "mqjdbc");
        // 这种topic持久化订阅者，需要先运行订阅一次，等于向消息服务中间件注册这个消费者，然后再运行客户端发送消息
        // 这个时候无论消费者是否在线，都会接收到，不在线的话，下次连接的时候，会把没有收过的消息都接收下来，类似微信的公众号订阅
        connection.start();
        // 订阅者获取消息
        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*****************订阅者收到的持久化topic："+textMessage.getText());
            // 进行轮询监听
            // 1号
            message = topicSubscriber.receive();

            // 2号
            // message = topicSubscriber.receive(3000L);
        }

        session.close();
        connection.close();

    }
}
