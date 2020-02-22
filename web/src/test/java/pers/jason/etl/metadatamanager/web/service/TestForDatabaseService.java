package pers.jason.etl.metadatamanager.web.service;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.connect.DatabaseConnector;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.exception.DatabaseServerConnectFailedException;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;

import java.util.Map;

@SpringBootTest
class TestForDatabaseService {

  @Autowired
  private DatabaseConnector databaseService;

  @Test
  public void successForTryConnectMySQL() {
    ConnectPioneer pioneer = new ConnectPioneer();
    pioneer.setUrl("localhost:3306");
    pioneer.setUsername("root");
    pioneer.setPassword("Hongdou@521");
    pioneer.setSchemaName("sys");
    pioneer.setDriverName(MetadataUtil.DRIVER_NAME_MYSQL);
    pioneer.setPlatformType(PlatformType.MYSQL);
    Map<String, String> props = Maps.newHashMap();
    props.put("serverTimezone", "UTC");
    pioneer.setProps(props);
    databaseService.tryConnect(pioneer);

    pioneer.setTableName("sys_config");
    databaseService.tryConnect(pioneer);

    try {
      pioneer.setTableName("xxxx");
      databaseService.tryConnect(pioneer);
    } catch (DatabaseServerConnectFailedException e) {
      assert "table [sys.xxxx] does not exist".equals(e.getMessage());
    }

    pioneer.setTableName(null);
    pioneer.setSchemaName("yyyy");
    try {
      databaseService.tryConnect(pioneer);
    } catch (Exception e) {

    }



  }

}
