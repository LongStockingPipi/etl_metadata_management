package pers.jason.etl.metadatamanager.core.synchronize.external;

public enum MySqlColumnType implements ExternalColumnType {

  INT(0, "int"),

  VARCHAR(1, "varchar"),

  DECIMAL(2, "decimal"),

  DATETIME(3, "datetime"),

  BLOB(4, "blob"),

  BINARY(5, "binary"),

  TINYBLOB(6, "tinyblob"),

  LONGBLOB(7, "longblob"),

  MEDIUMBLOB(8, "mediumblob"),

  VARBINARY(9, "varbinary"),

  DATE(10, "date"),

  TIME(11, "time"),

  TIMESTAMP(12, "timestamp"),

  YEAR(13, "year"),

  GEOMETRY(14, "geometry"),

  GEOMCOLLECTION(15, "geomcollection"),

  LINESTRING(16, "linestring"),

  MULTILINESTRING(17, "multilinestring"),

  MULTIPOINT(18, "multipoint"),

  MULTIPOLYGON(19, "multipolygon"),

  POINT(20, "point"),

  POLYGON(21, "polygon"),

  BIGINT(22, "bigint"),

  DOUBLE(23, "double"),

  FLOAT(24, "float"),

  MEDIUMINT(25, "mediumint"),

  SMALLINT(26, "smallint"),

  TINYINT(27, "tinyint"),

  JSON(28, "json"),

  CHAR(29, "char"),

  LONGTEXT(30, "longtext"),

  MEDIUMTEXT(31, "mediumtext"),

  TEXT(32, "text"),

  TINYTEXT(33, "tinytext"),

  BIT(34, "bit"),

  ENUM(35, "enum"),

  SET(36, "set");

  public Integer code;

  public String name;

  MySqlColumnType(Integer code, String name) {
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
