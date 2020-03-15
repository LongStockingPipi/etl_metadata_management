package pers.jason.etl.metadatamanager.core.synchronize.external;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Schema;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:17
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalSchema extends Schema {

  private Long platformId;

  private String initCommands;

  private String cName;

  private Set<ExternalTable> tableSet = Sets.newHashSet();

  @Override
  public <T extends Metadata> Set<T> findChild() {
    return (Set<T>) tableSet;
  }

  @Override
  public MetadataType returnMetadataType() {
    return MetadataType.SCHEMA;
  }

  @Override
  public void setParentId(Long id) {
    this.setPlatformId(id);
  }
}
