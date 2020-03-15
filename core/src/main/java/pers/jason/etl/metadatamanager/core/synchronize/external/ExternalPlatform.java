package pers.jason.etl.metadatamanager.core.synchronize.external;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:08
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPlatform extends Platform {

  private Integer typeCode;

  private Set<ExternalSchema> schemaSet = Sets.newHashSet();

  @Override
  public MetadataType returnMetadataType() {
    return MetadataType.PLATFORM;
  }

  @Override
  public <T extends Metadata> Set<T> findChild() {
    return (Set<T>) schemaSet;
  }

  @Override
  public void setParentId(Long id) {

  }

}
