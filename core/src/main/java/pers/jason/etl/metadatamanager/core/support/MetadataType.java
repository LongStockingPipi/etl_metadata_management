package pers.jason.etl.metadatamanager.core.support;

/**
 * @author Jason
 * @date 2020/2/20 2:00
 * @description
 */
public enum MetadataType {

  PLATFORM("platform", 0),
  SCHEMA("schema", 1),
  TABLE("table", 2),
  COLUMN("column", 3);

  public String name;

  public Integer code;

  MetadataType(String name, Integer code) {
    this.name = name;
    this.code = code;
  }
}
