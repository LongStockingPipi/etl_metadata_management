package pers.jason.etl.rest.service.impl;

import org.springframework.stereotype.Service;
import pers.jason.etl.rest.pojo.po.Platform;
import pers.jason.etl.rest.service.MetadataSynchronizeTemplate;

/**
 * @author Jason
 * @date 2020/2/18 22:16
 * @description
 */
@Service
public class MysqlMetadataSynchronizeTemplate extends MetadataSynchronizeTemplate {

  @Override
  protected Platform findDataFromLocal(Integer syncType, Long platformId, Long schemaId, Long tableId) {
    return null;
  }

  @Override
  protected Platform findDataFromRemote() {
    return null;
  }
}
