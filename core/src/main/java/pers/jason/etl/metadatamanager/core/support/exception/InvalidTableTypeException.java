package pers.jason.etl.metadatamanager.core.support.exception;

/**
 * @author Jason
 * @date 2020/2/18 0:38
 * @description
 */
public class InvalidTableTypeException extends RuntimeException {

  private String tableName;

  public InvalidTableTypeException(String tableName) {
    super("无效的表类型：" + tableName);
    this.tableName = tableName;
  }

  public String getInvalidTableTypeName() {
    return tableName;
  }
}
