package pers.jason.etl.metadatamanager.core.support.exception;

/**
 * @author Jason
 * @date 2020/2/18 0:21
 * @description
 */
public class InvalidFieldTypeException extends RuntimeException {

  private String typeName;

  public InvalidFieldTypeException(String typeName) {
    super("无效的字段类型：" + typeName);
    this.typeName = typeName;
  }

  public String getInvalidFieldTypeName() {
    return typeName;
  }
}
