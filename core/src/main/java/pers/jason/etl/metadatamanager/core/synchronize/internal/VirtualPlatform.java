package pers.jason.etl.metadatamanager.core.synchronize.internal;

import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/18 1:22
 * @description
 */
public class VirtualPlatform extends Platform {

  @Override
  public MetadataType returnMetadataType() {
    return MetadataType.PLATFORM;
  }

  @Override
  public <T extends Metadata> Set<T> getChild() {
    return null;
  }

  @Override
  public void setParentId(Long id) {

  }
}
