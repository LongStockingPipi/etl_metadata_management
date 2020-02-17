package pers.jason.etl.rest.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  private Set<ExternalSchema> schemaSet;

}
