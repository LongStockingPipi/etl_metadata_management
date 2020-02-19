package pers.jason.etl.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.jason.etl.rest.exception.InvalidServiceNameException;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.service.MetadataSynchronizeTemplate;

import java.util.Map;

/**
 * @author Jason
 * @date 2020/2/19 23:27
 * @description
 */
@Component
public class SynchronizeServiceHolder {

  private static final String SUFFIX_TEMPLATE_SERVICE_NAME = "SynchronizeTemplate";

  @Autowired
  private Map<String, MetadataSynchronizeTemplate> templateMap;

  public MetadataSynchronizeTemplate findMetadataSynchronizeService(PlatformType platformType) {
    String typeName = platformType.name;
    String serviceName = typeName + SUFFIX_TEMPLATE_SERVICE_NAME;
    MetadataSynchronizeTemplate synchronizeTemplate = templateMap.get(serviceName);
    if(null == synchronizeTemplate) {
      throw new InvalidServiceNameException(typeName);
    }
    return synchronizeTemplate;
  }
}
