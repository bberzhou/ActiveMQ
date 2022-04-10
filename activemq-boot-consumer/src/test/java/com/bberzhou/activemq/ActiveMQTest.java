package com.bberzhou.activemq;

import com.bberzhou.activemq.config.QueueConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.jms.JMSException;

/**
 * @description: 对queue进行测试
 * @author: bberzhou@gmail.com
 * @date: 4/10/2022
 * Create By Intellij IDEA
 */
@SpringBootTest(classes = {ConsumerMain.class})
// @RunWith(SpringJUnit4ClassRunner.class)
// 注意，@RunWith注解 在 JUnit 5.0之后，被 @ExtendWith()注解取代
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ActiveMQTest {
    @Resource
    private QueueConsumer queueConsumer;

    @Test
    public void testSend() throws JMSException {
        queueConsumer.receiveMsg();
    }
}
