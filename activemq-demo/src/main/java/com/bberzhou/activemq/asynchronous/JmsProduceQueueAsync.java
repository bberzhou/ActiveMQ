package com.bberzhou.activemq.asynchronous;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

/**
 * @description: 测试activeMQ的消息生产者，destination是queue，异步发送
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsProduceQueueAsync {


    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "queue-Async";

    public static void main(String[] args) throws JMSException {
        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 设置异步
        activeMQConnectionFactory.setUseAsyncSend(true);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5、创建消息的生产者
        ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer) session.createProducer(queue);
        TextMessage message = null;

        // 6、通过使用producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3 ; i++) {
            // 7、创建具体的消息
            // 这里createTextMessage() 方法类似创建一个字符串的消息，类比为学生按照要求写好的问题
            message = session.createTextMessage("message---"+i);
            // 设置消息id
            message.setJMSMessageID(UUID.randomUUID().toString()+"---------------order");
            String msgId = message.getJMSMessageID();
            // 异步send 的时候，注意有一个callback
            activeMQMessageProducer.send(message, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    System.out.println(msgId+"success to send");
                }

                @Override
                public void onException(JMSException e) {
                    System.out.println(msgId+"fail to send");
                }
            });
            // 8、通过消息生产者 producer 发送给MQ

        }

        // 9、关闭资源
        activeMQMessageProducer.close();
        session.close();
        connection.close();
        System.out.println("********* 生产者消息异步发布到MQ完成**************************************");
    }
}
