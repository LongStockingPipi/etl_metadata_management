package pers.jason.etl.rest.pojo.po;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.rest.pojo.MetadataType;

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

  private Boolean writable;

  private Integer type;

  private Set<ExternalColumn> columnSet = Sets.newHashSet();

  @Override
  public <T extends Metadata> Set<T> getChild() {
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
