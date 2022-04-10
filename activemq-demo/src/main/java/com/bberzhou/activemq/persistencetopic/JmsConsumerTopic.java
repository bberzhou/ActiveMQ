package com.bberzhou.activemq.persistencetopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试MQ的消息消费者，destination是topic
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerTopic {
    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String TOPIC_NAME = "Topic-Persist";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号topic消息订阅者");
        // System.out.println("我是2号topic消息订阅者");

        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        // 设置连接id
        connection.setClientID("1号");
        // connection.setClientID("2号");

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        // 创建一个持久化的订阅者
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark...");

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

        /**
         *  1、先生产消息，只启动1号消费者。问题：1号消费者能消费消息吗？
         *      肯定能
         *
         *   2、先生产消息，先启动1号消费者再启动2号消费者，问题：2号消费者还能消费消息吗？
         *      1号可以消费，但是2号不能消费，因为1号已经消费完了
         *
         *   3、先启动2个消费者，再生产6条消息，这时消费情况如何？
         *      （1）2个消费者都有6条
         *      （2）先到先得，6条消息全部给一个
         *      （3）一人一半，平均分配。======》采用的是这种机制，并且如果队列里面有多个消费者，就是按照轮询的方式进行分配
         *
         *   4、如果MQ出现宕机的情况，那么消息的持久化和丢失情况分别是如何的？
         *
         *
         *
         *      非持久化：当服务器宕机时，消息就不存在
         *          messageProducer。setDeliveryMode(DeliveryMode.NON_PERSISTENT)
         *          topic默认是非持久化的
         *
         *      持久化：当服务器宕机，消息依然存在
         *          messageProducer。setDeliveryMode(DeliveryMode.PERSISTENT)
         *          queue默认是持久化的
         *
         *    非持久订阅状态下，不能回复或者重新派送一个未签收的消息
         *    持久订阅下才能恢复或者重新派送一个未签收的消息
         *
         *    当所有的消息都要被接收时，采用持久订阅，
         *    当丢失消息能够被接受时候，采用非持久订阅
         *
         */
    }
}
