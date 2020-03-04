package pers.jason.etl.metadatamanager.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.api.MqConfig;

/**
 * @author Jason
 * @date 2020/3/4 15:11
 * @description
 */
@SpringBootTest
public class MqReceiverTest {

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Test
  public void testFroSendMessage() {
    amqpTemplate.convertAndSend(MqConfig.QUEUE_MQ_METADATA_NAME, "Hello Mq");
  }

}
