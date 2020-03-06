package pers.jason.etl.metadatamanager.core.support;

public enum PlatformType {

  MYSQL(0, "mysql"),

  ORACLE(1, "oracle"),

  HIVE(2, "hive");

  public Integer code;

  public String name;

  PlatformType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }
}
