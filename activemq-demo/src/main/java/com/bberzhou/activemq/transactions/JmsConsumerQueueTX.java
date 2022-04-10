package com.bberzhou.activemq.transactions;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试MQ的消息消费者，destination是queue，演示事务
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerQueueTX {


    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "queueTX";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消息消费者");

        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收，Session.AUTO_ACKNOWLEDGE 自动签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        // Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        /**
         *      自动签收
         *      int AUTO_ACKNOWLEDGE = 1;
         *      手动签收
         *     int CLIENT_ACKNOWLEDGE = 2;
         *     可以运行部分重复签收
         *     int DUPS_OK_ACKNOWLEDGE = 3;
         *     事务级，一般开启事务之后，这个作用就不大
         *     int SESSION_TRANSACTED = 0;
         */

        // 一、消息的签收，主要分为事务下的和非事务下的
        // 1.1 非事务下
        // 非事务的手动签收，如果没有对消息进行签收的，那么可能出现重复获取消息的情况，所以需要在获得消息后，客户端手动签收
        // Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // 非事务的自动签收
        // Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 非事务的允许重复签收
        // Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);

        // 1.2 事务下的
        //  事务下的自动签收，这时候就需要关闭前提交事务，session.commit()；
        //
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
                    System.out.println("***************消费者接收到 TextMessage  TX 消息********"+textMessage.getText());

                    // 手动对消息进行签收
                    // textMessage.acknowledge();

                }

            }
        });
        // 保证控制台不关闭，直到消费完
        System.in.read();
        messageConsumer.close();
        // 消费者端，也提交事务,如果消费者端开启事务（true）之后，这里不进行提交，则会重现重复消费的情况
        // 不执行commit方法，这些消息不会标记已消费，下次还会被消费。
        // session.commit();
        session.close();
        connection.close();
        /**
         *  对于消费端：
         *  （1） 如果事务设置为false的情况下，自动签收消息，不会出现重复获取的情况，
         *        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         *  （2） 如果手动签收，则需要在获取消息后手动签收，否则会出现重复获取消息的情况
         *       Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
         *       textMessage.acknowledge();
         *
         *  （3）如果事务设置为true,此时事务的优先级要大于签收，签收可以随意，但是需要提交事务
         *      Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
         *      Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
         *      # textMessage.acknowledge();
         *       session.commit();
         *
         *  （4）如果事务设置为true，但是未提交(session.commit)，则会出现重复获取消息的情况
         *      Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
         *      Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
         *      #session.commit();
         *
         *      所以如果需要签收的话，在消费端需要先关闭事务，否则事务的优先级更高
         *
         *    总结：
         *      在事务性的会话中，当一个事务被成功提交则消息被自动签收，如果消息回滚，则消息会被再次传送
         *      非事务性的会话中，消息合适被确认取决于创建会话时的应答模式（acknowledge mode）
         */

    }
}
