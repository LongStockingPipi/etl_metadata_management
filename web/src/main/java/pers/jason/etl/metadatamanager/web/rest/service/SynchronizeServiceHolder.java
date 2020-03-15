package pers.jason.etl.metadatamanager.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.jason.etl.metadatamanager.core.connect.DatabaseConnector;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.web.exception.InvalidServiceNameException;

import java.util.Map;

/**
 * @author Jason
 * @date 2020/2/19 23:27
 * @description
 */
@Component
public class SynchronizeServiceHolder {

  private static final String SUFFIX_TEMPLATE_SERVICE_NAME = "MetadataSynchronizeService";

  private static final String SUFFIX_METADATA_CRUD_SERVICE_NAME = "MetadataServiceImpl";

  private static final String SUFFIX_DATABASE_CONNECTOR_NAME = "Connector";

  @Autowired
  private Map<String, MetadataSynchronizeTemplate> templateMap;

  @Autowired
  private Map<String, MetadataCrudService> metadataCrudServiceMap;

  @Autowired
  private Map<String, DatabaseConnector> connectorMap;

  public DatabaseConnector findDatabaseConnector(PlatformType platformType) {
    String typeName = platformType.name;
    String serviceName = typeName + SUFFIX_DATABASE_CONNECTOR_NAME;
    DatabaseConnector connector = connectorMap.get(connectorMap);
    if(null == connector) {
      throw new InvalidServiceNameException(serviceName);
    }
    return connector;
  }

  public MetadataCrudService findMetadataCrudService(PlatformType platformType) {
    String typeName = platformType.name;
    String prefix = null;
    if(PlatformType.MYSQL.equals(platformType) || PlatformType.MYSQL.equals(platformType)) {
      prefix = "external";
    }
    String serviceName = prefix + SUFFIX_METADATA_CRUD_SERVICE_NAME;
    MetadataCrudService crudService = metadataCrudServiceMap.get(serviceName);
    if(null == crudService) {
      throw new InvalidServiceNameException(serviceName);
    }
    return crudService;
  }

  public MetadataSynchronizeTemplate findMetadataSynchronizeService(PlatformType platformType) {
    String typeName = platformType.name;
    String serviceName = typeName + SUFFIX_TEMPLATE_SERVICE_NAME;
    MetadataSynchronizeTemplate synchronizeTemplate = templateMap.get(serviceName);
    if(null == synchronizeTemplate) {
      throw new InvalidServiceNameException(serviceName);
    }
    return synchronizeTemplate;
  }
}
