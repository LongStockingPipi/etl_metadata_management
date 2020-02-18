package pers.jason.etl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import pers.jason.etl.rest.dao.ExternalPlatformDao;
import pers.jason.etl.rest.pojo.po.ExternalPlatform;

/**
 * @author Jason
 * @date 2020/2/19 0:43
 * @description
 */
@SpringBootTest
public class TestForMetadataDao {

  @Autowired
  private ExternalPlatformDao platformDao;

  @Test
  public void successForSearchPlatform() {
    Long platformId = 2L;
    ExternalPlatform platform = platformDao.findPlatform(platformId);
    Assert.notNull(platform, "Insert an ID = " + platformId + " into the table named 't_etl_metadata_external_platform' before testing");
  }
}
