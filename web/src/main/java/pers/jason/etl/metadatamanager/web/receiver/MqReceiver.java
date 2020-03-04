package pers.jason.etl.metadatamanager.web.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pers.jason.etl.metadatamanager.api.MqConfig;

/**
 * @author Jason
 * @date 2020/3/4 14:54
 * @description 接受RabbitMQ同步请求消息
 */
@Slf4j
@Component
public class MqReceiver {

//  @RabbitListener(queues = MqConfig.QUEUE_MQ_METADATA_SYNC_NAME)
  @RabbitListener(queuesToDeclare = @Queue(MqConfig.QUEUE_MQ_METADATA_NAME))
//  @RabbitListener(bindings = @QueueBinding(
//      value = @Queue(MqConfig.QUEUE_MQ_METADATA_NAME),
//      exchange = @Exchange(MqConfig.QUEUE_EXCHANGE_SYNC)
//  ))
  public void process(String message) {
    log.info(message);
    log.info("hehe");
    System.out.println(message);
  }

}
