package com.bberzhou.activemq.queue;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 测试MQ的消息消费者，destination是queue
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsConsumerQueue {
    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是3号消息消费者");

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
         * 同步阻塞方式（ receive() ）
         * 订阅者或者接收者调用 MessageConsumer的receive() 方法来接收消息，receive()方法在能够接收到消息之前（或者超时之前）将会一直阻塞着
         * 如果加上时间参数就是“过时不候”
         */

        // while (true){
        //     // receive()方法，如果带参数，(long var1)，就是如果超过一定时间就不再继续接收消息，并且会关闭掉
        //     // 默认就是一直wait，只要有消息进来就会进行处理，并且不会关闭连接
        //     // Message message = messageConsumer.receive(10);
        //     Message message = consumer.receive();
        //     // 并将消息进行类型转换，因为生产者生产的是TextMessage
        //     TextMessage textMessage = (TextMessage) message;
        //     if (textMessage != null){
        //         System.out.println("****************消费者接收到消息********************"+textMessage.getText());
        //     }else {
        //         // 如果消息接收完毕，就跳出循环
        //         break;
        //     }
        // }
        // // 按顺序关闭
        // consumer.close();
        // session.close();
        // connection.close();

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
                    // 通过key来获取消息的属性
                    System.out.println("***************消费者接收到 TextMessage 消息属性********************"+textMessage.getStringProperty("attr1"));

                }
                // 如果是 MapMessage，要转换成对应的消息类型
                // 注意发送和接收的消息体类型，必须要一致
                if (message instanceof MapMessage){
                    // 强转一下
                    MapMessage mapMessage = (MapMessage) message;
                    System.out.println("****************消费者接收到 MapMessage 消息********************"+mapMessage.getString("key1"));
                }
            }
        });
        // 保证控制台不关闭，直到消费完

        System.in.read(); // press any key to exit
        messageConsumer.close();
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
         *
         */
    }
}
