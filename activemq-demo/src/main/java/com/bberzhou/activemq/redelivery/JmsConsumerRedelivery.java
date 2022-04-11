package com.bberzhou.activemq.redelivery;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试消息重发机制
 * @author: bberzhou@gmail.com
 * @date: 4/11/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerRedelivery {

    /**
     *  具体哪些情况引起消息重发：
     *  （1）Client用了transactions且再session中调用了rollback
     *  （2）Client用了transactions且再调用commit之前关闭或者没有commit
     *  （3）Client再CLIENT_ACKNOWLEDGE的传递模式下，session中调用了recover
     */

    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "queue-delivery";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是3号消息消费者");

        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 重发策略主要是在消费者端
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        // 设置最大重发次数，注意实际上是 1+3，不包括第一次发送的
        redeliveryPolicy.setMaximumRedeliveries(3);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode),
        // 这里开启事务
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);

        // 5、创建消息的消费者，需要找相同的destination，queue或者topic
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /**
         *  通过监听的方式（Listener）
         *
         *  setMessageListener()方法里面需要掺入一个消息监听器，可以通过匿名内部类，
         */
        messageConsumer.setMessageListener(new MessageListener() {
            @SneakyThrows
            @Override
            public void onMessage(Message message) {
                // 判断队列里面的消息是否已经消费完，即为空
                if (message instanceof TextMessage){
                    // 强转一下
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("*************** 消费者接收到 TextMessage 消息***************"+textMessage.getText());

                }
            }
        });
        System.in.read(); // press any key to exit
        messageConsumer.close();
        // 如果开启了事务，就需要在这里进行commit
        // session.commit();
        session.close();
        connection.close();

    }
}
