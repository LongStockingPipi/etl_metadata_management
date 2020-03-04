package pers.jason.etl.metadatamanager.plugins.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @date 2020/3/4 15:08
 * @description
 */
@Component
public class MqSender {

  @Autowired
  private AmqpTemplate amqpTemplate;

}
