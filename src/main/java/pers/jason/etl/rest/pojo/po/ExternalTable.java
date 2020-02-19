package pers.jason.etl.rest.pojo.po;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:36
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalTable extends Metadata {

  private Long schemaId;

  private String initCommands;

  private String cName;

  private Boolean writable;

  private Integer type;

  private Set<ExternalColumn> columnSet = Sets.newHashSet();
}
