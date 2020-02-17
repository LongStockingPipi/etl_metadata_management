package pers.jason.etl.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jason
 * @date 2020/2/18 1:26
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectPioneer {

  private String url;

  private String username;

  private String password;

  private String schemaName;

  private String tableName;

  private String driverName;

}
