package pers.jason.etl.rest.pojo.po;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/17 23:17
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalSchema extends Metadata {

  private Long platformId;

  private String initCommands;

  private String cName;

  private Set<ExternalTable> tableSet = Sets.newHashSet();
}
