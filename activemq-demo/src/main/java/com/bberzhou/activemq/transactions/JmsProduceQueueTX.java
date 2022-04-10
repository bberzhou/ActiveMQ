package com.bberzhou.activemq.transactions;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: 测试activeMQ的消息生产者，destination是queue，演示事务
 * @author: bberzhou@gmail.com
 * @date: 4/9/2022
 * Create By Intellij IDEA
 */
public class JmsProduceQueueTX {

    public static final String ACTIVEMQ_URL = "tcp://192.168.60.130:61616";
    public static final String QUEUE_NAME = "queueTX";

    public static void main(String[] args) throws JMSException {
        // 1、创建activemq连接工厂，按照给定的url地址，采用默认的用户名和密码（admin admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、通过连接工厂 ， 获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话session
        // 第一个参数是事务，第二个参数是签收
        // public Session createSession(boolean transacted, int acknowledgeMode)
        /**
         * 生产者端的事务设置
         *  false:
         *      只要执行send，就会将消息提交到队列中去
         *      如果关闭事务，那第2个签收参数的设置需要有效
         *
         *  true:
         *      先执行send再commit，消息才会被真正的提交到队列中去
         *      消息需要批量发送，需要缓冲区处理
         *
         */
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // 4、创建具体的目的地，destination（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);

        // 5、创建消息的生产者
        MessageProducer producer = session.createProducer(queue);

        // 6、通过使用producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3 ; i++) {
            // 7、创建具体的消息
            // 这里createTextMessage() 方法类似创建一个字符串的消息，类比为学生按照要求写好的问题
            TextMessage textMessage = session.createTextMessage("发送的消息 queue TX msg " + i);
            // 8、通过消息生产者 producer 发送给MQ
            producer.send(textMessage);

        }

        // 9、关闭资源
        producer.close();
        // 如果开启了事务 ,设置为true，那么就需要再关闭session之前，进行提交，这样消息才会保存到队列中
        session.commit();
        session.close();
        connection.close();
        System.out.println("***************************queue 生产者 TX 消息发布到MQ完成**************************************");

        /**
         *  事务偏向于生产者，签收偏向于消费者
         *
         *
         */

        // 事务的用法，保证复杂业务下的一致性
        // try{
        //     // 逻辑ok时候，提交所有的消息到队列中
        //     session.commit();
        // }catch (Exception e){
        //     e.printStackTrace();
        //     // 如果出现异常，需要进行回滚操作
        //     session.rollback();
        // }finally {
        //     session.close();
        // }
    }
}
