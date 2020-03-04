package pers.jason.etl.metadatamanager.web.rest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.connect.DatabaseConnector;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.support.util.PlatformTypeUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalPlatformDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalSchemaDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalTableDao;
import pers.jason.etl.metadatamanager.web.rest.request.SynchronyRequest;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataService;
import pers.jason.etl.metadatamanager.web.rest.service.SynchronizeServiceHolder;

/**
 * @author Jason
 * @date 2020/2/17 22:53
 * @description
 */
@Slf4j
@Service
public class MetadataServiceImpl implements MetadataService {

  @Autowired
  private SynchronizeServiceHolder synchronizeServiceHolder;

  @Autowired
  private DatabaseConnector databaseConnector;

  @Autowired
  private ExternalPlatformDao externalPlatformDao;

  @Autowired
  private ExternalSchemaDao externalSchemaDao;

  @Autowired
  private ExternalTableDao externalTableDao;

  @Override
  public void syncMetadata(SynchronyRequest request) {
    Integer platformTypeCode = request.getPlatformType();
    PlatformType platformType = PlatformTypeUtil.getPlatformTypeByCode(platformTypeCode);

    //检测是否可连接
    Long platformId = request.getPlatformId();
    Long schemaId = request.getSchemaId();
    Long tableId = request.getTableId();
    Platform platform = externalPlatformDao.findPlatform(platformId);
    String schemaName = externalSchemaDao.findSchema(schemaId).getName();
    String tableName = externalTableDao.findTable(tableId).getName();
    ConnectPioneer pioneer = getPioneerByPlatform(platformType, platform, schemaName, tableName);
    databaseConnector.tryConnect(pioneer);

    MetadataSynchronizeTemplate synchronizeTemplate = synchronizeServiceHolder.findMetadataSynchronizeService(platformType);
    SynchronizeModel synchronizeModel = new SynchronizeModel(
        platformId, schemaId, tableId, pioneer.getUrl(), pioneer.getUsername(), pioneer.getPassword()
        , pioneer.getDriverName(), schemaName, tableName);
    Long start = System.currentTimeMillis();
    try {
      synchronizeTemplate.synchronize(synchronizeModel);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("synchronous data completion! It takes " + (System.currentTimeMillis() - start) + " milliseconds");
  }

  private ConnectPioneer getPioneerByPlatform(PlatformType platformType, Platform platform, String schemaName, String tableName) {
    ConnectPioneer pioneer = new ConnectPioneer();
    pioneer.setUrl(platform.getUrl());
    pioneer.setUsername(platform.getUsername());
    pioneer.setPassword(platform.getPasswd());
    pioneer.setSchemaName(schemaName);
    pioneer.setTableName(tableName);
    pioneer.setDriverName(MetadataUtil.getDriverNameByType(platformType));
    pioneer.setPlatformType(platformType);
    return pioneer;
  }


}
