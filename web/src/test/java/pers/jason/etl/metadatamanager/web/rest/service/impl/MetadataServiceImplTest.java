package pers.jason.etl.metadatamanager.web.rest.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.connect.impl.MySqlConnector;
import pers.jason.etl.metadatamanager.core.connect.impl.OracleConnector;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.web.rest.service.CacheService;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataService;

@SpringBootTest
class MetadataServiceImplTest {

  @Autowired
  private MetadataService metadataService;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private OracleConnector oracleConnector;

  @Autowired
  private MySqlConnector mySqlConnector;

  @Test
  public void testForFindPlatformById() {
    Long platformId = 3L;
    String fullName = MetadataUtil.getExternalMetadataFullName(MetadataType.PLATFORM, platformId, null
        , null, null, null, null, 0);
    cacheService.del(fullName);

    Platform platform = metadataService.findPlatformById(platformId);
    Assert.notNull(platform, "platform can not be null!");

    Platform platform1 = (Platform) cacheService.getObj(fullName).orElse(null);
    Assert.notNull(platform1, "metadata in cache can not be null");
    assert (platform.getUrl() + platform.getUsername()).equals(platform1.getUrl() + platform1.getUsername());
  }

  @Test
  public void testForConnectDB() {
    ConnectPioneer connectPioneer1 = new ConnectPioneer();
    connectPioneer1.setPlatformType(PlatformType.ORACLE);
    connectPioneer1.setUrl("jdbc:oracle:thin:@//localhost:1521/helowin");
    connectPioneer1.setUsername("system");
    connectPioneer1.setPassword("oracle");
    connectPioneer1.setSchemaName("JZH");
    connectPioneer1.setDriverName("oracle.jdbc.driver.OracleDriver");
    //tableName
    oracleConnector.tryConnect(connectPioneer1);
  }

}