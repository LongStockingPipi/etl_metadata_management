package pers.jason.etl.metadatamanager.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import pers.jason.etl.metadatamanager.core.support.props.CoreProperties;
import pers.jason.etl.metadatamanager.web.props.SimpleProperties;

/**
 * @author Jason
 * @date 2020/3/7 1:43
 * @description
 */
@SpringBootTest
public class BeanTest {

  @Autowired
  private CoreProperties coreProperties;

  @Autowired
  private SimpleProperties simpleProperties;

  @Test
  public void testForBeanAutowired() {
    Assert.notNull(simpleProperties, "bean simpleProperties");
    assert simpleProperties.getDistributedLockTimeout() == 15000L;
    assert simpleProperties.getLockExpireTime() == 10L;

    Assert.notNull(coreProperties, "bean coreProperties不能为空");
    System.out.println(coreProperties);
  }
}
