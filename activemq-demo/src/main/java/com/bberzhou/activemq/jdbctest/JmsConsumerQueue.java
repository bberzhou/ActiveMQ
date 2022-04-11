package com.bberzhou.activemq.jdbctest;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试MQ的消息消费者，destination是queue，测试JDBC持久存储
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerQueue {
    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "jdbc01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消息消费者");

        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

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
                    System.out.println("***************消费者接收到 TextMessage 消息********************"+textMessage.getText());

                }
            }
        });
        // 保证控制台不关闭，直到消费完

        System.in.read(); // press any key to exit
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
