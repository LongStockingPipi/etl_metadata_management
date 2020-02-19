package pers.jason.etl.service;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import pers.jason.etl.rest.exception.DatabaseServerConnectFailedException;
import pers.jason.etl.rest.pojo.ConnectPioneer;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.service.DatabaseService;
import pers.jason.etl.rest.utils.MetadataUtil;

import java.sql.SQLException;
import java.util.Map;

@SpringBootTest
class TestForDatabaseService {

  @Autowired
  private DatabaseService databaseService;

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
    assert databaseService.connect(pioneer);

    pioneer.setTableName("sys_config");
    assert databaseService.connect(pioneer);

    try {
      pioneer.setTableName("xxxx");
      databaseService.connect(pioneer);
    } catch (DatabaseServerConnectFailedException e) {
      assert "table [sys.xxxx] does not exist".equals(e.getMessage());
    }

    pioneer.setTableName(null);
    pioneer.setSchemaName("yyyy");
    assert !databaseService.connect(pioneer);



  }

}
