package pers.jason.etl.rest.pojo;

public enum SynchronizeType {

  LEVEL_PLATFORM(0, "levelPlatform"),

  LEVEL_SCHEMA(1, "levelSchema"),

  LEVEL_TABLE(2, "levelTable");

  private Integer code;

  private String type;

  SynchronizeType(Integer code, String type) {
    this.code = code;
    this.type = type;
  }
}
