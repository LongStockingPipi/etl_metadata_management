package pers.jason.etl.metadatamanager.web.rest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.connect.DatabaseConnector;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.support.props.CoreProperties;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.support.util.PlatformTypeUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.core.synchronize.internal.VirtualPlatform;
import pers.jason.etl.metadatamanager.web.props.SimpleProperties;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalPlatformDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalSchemaDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalTableDao;
import pers.jason.etl.metadatamanager.web.rest.request.SynchronyRequest;
import pers.jason.etl.metadatamanager.web.rest.service.CacheService;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataService;
import pers.jason.etl.metadatamanager.web.rest.service.SynchronizeServiceHolder;

import java.util.concurrent.TimeUnit;

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
  private ExternalPlatformDao externalPlatformDao;

  @Autowired
  private ExternalSchemaDao externalSchemaDao;

  @Autowired
  private ExternalTableDao externalTableDao;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private CoreProperties coreProperties;

  @Override
  public void syncMetadata(SynchronyRequest request) {
    Integer platformTypeCode = request.getPlatformType();
    PlatformType platformType = PlatformTypeUtil.getPlatformTypeByCode(platformTypeCode);

    //检测是否可连接
    Long platformId = request.getPlatformId();
    Long schemaId = request.getSchemaId();
    Long tableId = request.getTableId();
    Platform platform = findPlatformById(platformId);
    String schemaName = externalSchemaDao.findSchema(schemaId).getName();
    String tableName = externalTableDao.findTable(tableId).getName();
    ConnectPioneer pioneer = getPioneerByPlatform(platformType, platform, schemaName, tableName);
    synchronizeServiceHolder.findDatabaseConnector(platformType).tryConnect(pioneer);

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

  @Override
  public Platform findPlatformById(Long platformId) {
    if(null == platformId) {
      //todo 装在虚拟平台对象
      return new VirtualPlatform();
    }
    String fullName = MetadataUtil.getExternalMetadataFullName(MetadataType.PLATFORM, platformId, null
        , null, null, null, null, 0);
    Platform platform = (Platform) cacheService.getObj(fullName).orElse(null);
    if(null == platform) {
      Long timeout = ((SimpleProperties) coreProperties.getSimple()).getMetadataExpireTime();
      platform = externalPlatformDao.findPlatform(platformId);
      if(null != platform) {
        cacheService.setObj(fullName, platform, timeout, TimeUnit.SECONDS);
      }
    }
    return platform;
  }






}
