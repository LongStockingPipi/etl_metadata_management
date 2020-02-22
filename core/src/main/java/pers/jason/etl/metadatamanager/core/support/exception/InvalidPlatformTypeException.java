package pers.jason.etl.metadatamanager.core.support.exception;

/**
 * @author Jason
 * @date 2020/2/18 0:21
 * @description
 */
public class InvalidPlatformTypeException extends RuntimeException {

  private Integer typeCode;

  private String typeName;

  public InvalidPlatformTypeException(Integer typeCode) {
    super("无效的平台类型：" + typeCode);
    this.typeCode = typeCode;
  }

  public InvalidPlatformTypeException(String typeName) {
    super("无效的平台类型：" + typeName);
    this.typeName = typeName;
  }

  public String getInvalidPlatformTypeName() {
    return typeName;
  }

  public Integer getInvalidPlatformTypeCode() {
    return typeCode;
  }
}
