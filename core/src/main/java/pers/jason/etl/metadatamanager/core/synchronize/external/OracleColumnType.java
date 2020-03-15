package pers.jason.etl.metadatamanager.core.synchronize.external;

/**
 * @author Jason
 * @date 2020/3/15 18:26
 * @description
 */
public enum  OracleColumnType implements ExternalColumnType {

  VARCHAR2(0, "varchar2"),

  NUMBER(1, "number"),

  DATE(2, "date");

  public Integer code;

  public String name;

  OracleColumnType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }


  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }
}
