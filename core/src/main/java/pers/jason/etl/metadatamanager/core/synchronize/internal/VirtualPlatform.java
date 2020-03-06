package pers.jason.etl.metadatamanager.core.synchronize.internal;

import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalPlatform;

/**
 * @author Jason
 * @date 2020/2/18 1:22
 * @description 虚拟平台，因为hive没有平台概念，方便同步操作
 */
public class VirtualPlatform extends ExternalPlatform {

  {
    this.setTypeCode(PlatformType.HIVE.code);
  }

}
