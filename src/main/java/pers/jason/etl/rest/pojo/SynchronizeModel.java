package pers.jason.etl.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jason
 * @date 2020/2/20 2:31
 * @description
 */
@Data
@NoArgsConstructor
public class SynchronizeModel {

  private Long platformId;

  private Long schemaId;

  private Long tableId;

  private String url;

  private String username;

  private String password;

  private String driverName;

  private String schemaName;

  private String tableName;

  public SynchronizeModel(
      Long platformId, Long schemaId, Long tableId
      , String url, String username, String password, String driverName
      , String schemaName, String tableName) {
    this.platformId = platformId;
    this.schemaId = schemaId;
    this.tableId = tableId;
    this.url = url;
    this.username = username;
    this.password = password;
    this.driverName = driverName;
    this.schemaName = schemaName;
    this.tableName = tableName;
  }
}
