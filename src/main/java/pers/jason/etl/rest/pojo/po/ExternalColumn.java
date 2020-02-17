package pers.jason.etl.rest.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jason
 * @date 2020/2/17 23:39
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalColumn extends Metadata {

  private Long tableId;

  private String cName;

  private Integer type;

  //是否是分区字段
  private Boolean partitionField;

  //是否是主键
  private Boolean primaryKey;

  //是否可以为空
  private Boolean nullable;

  private Long maxLength;

  //小数位数
  private Long numericScale;

  private Integer position;

}
