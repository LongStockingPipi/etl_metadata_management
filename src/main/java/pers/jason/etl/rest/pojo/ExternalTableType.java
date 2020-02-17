package pers.jason.etl.rest.pojo;

public enum ExternalTableType {

  BASE_TABLE(0, "BASE TABLE"),
  VIEW(1, "VIEW");

  private Integer code;

  private String name;

  ExternalTableType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }
}
