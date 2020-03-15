package pers.jason.etl.metadatamanager.core.synchronize.external;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Table;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:36
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalTable extends Table {

  private Long schemaId;

  private String initCommands;

  private String cName;

  private Boolean writable = true;

  private Integer type;

  private Set<ExternalColumn> columnSet = Sets.newHashSet();

  @Override
  public <T extends Metadata> Set<T> findChild() {
    return (Set<T>) columnSet;
  }

  @Override
  public MetadataType returnMetadataType() {
    return MetadataType.TABLE;
  }

  @Override
  public void setParentId(Long id) {
    this.setSchemaId(id);
  }
}
