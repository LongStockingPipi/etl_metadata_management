package pers.jason.etl.rest.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.jason.etl.rest.pojo.MetadataType;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:39
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalColumn extends Column {

  private Long tableId;

  private String cName;

  private Integer type;

  //是否是分区字段
  private Boolean partitionField = false;

  //是否是主键
  private Boolean primaryKey;

  //是否可以为空
  private Boolean nullable;

  private Long maxLength;

  //小数位数
  private Long numericScale;

  private Integer position;

  @Override
  public <T extends Metadata> Set<T> getChild() {
    return null;
  }

  @Override
  public void setParentId(Long id) {
    this.setTableId(id);
  }

  @Override
  public MetadataType returnMetadataType() {
    return MetadataType.COLUMN;
  }

}
