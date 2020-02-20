package pers.jason.etl.rest.pojo.po;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.rest.pojo.MetadataType;

import java.util.Map;
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
  public <T extends Metadata> Set<T> getChild() {
    return (Set<T>) schemaSet;
  }

  @Override
  public void setParentId(Long id) {

  }

}
