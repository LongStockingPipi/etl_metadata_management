package pers.jason.etl.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.jason.etl.rest.dao.ExternalPlatformDao;
import pers.jason.etl.rest.dao.ExternalSchemaDao;
import pers.jason.etl.rest.dao.ExternalTableDao;
import pers.jason.etl.rest.pojo.ConnectPioneer;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.pojo.SynchronizeModel;
import pers.jason.etl.rest.pojo.po.Platform;
import pers.jason.etl.rest.request.SynchronyRequest;
import pers.jason.etl.rest.service.DatabaseService;
import pers.jason.etl.rest.service.MetadataService;
import pers.jason.etl.rest.service.MetadataSynchronizeTemplate;
import pers.jason.etl.rest.utils.MetadataUtil;
import pers.jason.etl.rest.utils.PlatformTypeUtil;

/**
 * @author Jason
 * @date 2020/2/17 22:53
 * @description
 */
@Service
public class MetadataServiceImpl implements MetadataService {

  private static final Logger logger = LoggerFactory.getLogger(MetadataServiceImpl.class);

  @Autowired
  private SynchronizeServiceHolder synchronizeServiceHolder;

  @Autowired
  private DatabaseService databaseService;

  @Autowired
  private ExternalPlatformDao externalPlatformDao;

  @Autowired
  private ExternalSchemaDao externalSchemaDao;

//  @Autowired
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
    databaseService.connect(pioneer);

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
    logger.info("synchronous data completion! It takes " + (System.currentTimeMillis() - start) + " milliseconds");
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
