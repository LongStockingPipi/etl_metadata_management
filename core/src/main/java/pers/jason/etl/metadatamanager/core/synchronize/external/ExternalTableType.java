package pers.jason.etl.metadatamanager.core.synchronize.external;

public enum ExternalTableType {

  BASE_TABLE(0, "BASE TABLE"),
  VIEW(1, "VIEW"),
  SYSTEM_VIEW(2, "SYSTEM VIEW"),
  TABLE(3, "TABLE");

  public Integer code;

  public String name;

  ExternalTableType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }
}
