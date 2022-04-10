package com.bberzhou.activemq.topic;

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

    // public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String TOPIC_NAME = "topic_bberzhou";

    public static void main(String[] args) throws JMSException {
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
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的生产者
        //  同时设置消息的目的地
        MessageProducer producer = session.createProducer(topic);

        // 6、通过使用producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 5 ; i++) {
            // 7、创建具体的消息
            // 这里createTextMessage() 方法类似创建一个字符串的消息，类比为学生按照要求写好的问题
            TextMessage textMessage = session.createTextMessage("发送的消息 topic msg " + i);

            // 消息主要分为消息头、消息体、消息属性

            //  1、可以设置消息传递模式，持久或者非持久模式
            // textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);

            //  2、可以设置消息的过期时间，默认情况下，为了高可用是永不过期的
            // 消息过期时间，等于Destination的send方法中的timeToLive值加上发送时刻的GMT时间值
            // 如果timeToLive的值等于0，则JMSExpiration被设置为0，表示该消息永远不会过期
            // 如果消息发送之后，在消息过期时间之内，还没有被发送到目的地，则该消息就会被清除掉

            textMessage.setJMSExpiration(10000);

            /*  3、消息优先级，从0-9十个级别，0-4是普通消息5-9是加急消息。
                JMS不要求MQ严格按照这十个优先级发送消息但必须保证加急消息要先于普通消息到达。默认是4级。
             */
            textMessage.setJMSPriority(10);

            // 唯一标识每个消息的标识。MQ会给我们默认生成一个，我们也可以自己指定。在分布式中系统中，可以唯一标识

            textMessage.setJMSMessageID("ABCD");

            // 8、通过消息生产者 producer 发送给MQ，还可以在这个send()方法里面传入一些属性
            producer.send(textMessage);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("key1","mapMessage -----------value1");
            producer.send(mapMessage);



        }

        // 9、关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("***************************topic 生产者消息发布到MQ完成**************************************");
    }
}
