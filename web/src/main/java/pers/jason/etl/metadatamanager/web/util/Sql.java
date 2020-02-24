package pers.jason.etl.metadatamanager.web.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/22 23:10
 * @description
 */
public class Sql {

  public static final String STATIC_KEY_YES = "YES";

  public static final String STATIC_KEY_NO = "NO";

  public static final String SQL_MYSQL_FIND_METADATA_BY_PLATFORM =
      " select s.schema_name as schema_name, t.table_name as table_name " +
          ", t.table_comment, c.column_name as column_name, t.table_type " +
          ", c.ordinal_position, c.numeric_precision, c.numeric_scale, c.is_nullable " +
          ", c.data_type, c.column_key, c.character_maximum_length, c.column_comment " +
          "from INFORMATION_SCHEMA.SCHEMATA s " +
          "left join INFORMATION_SCHEMA.TABLES t on s.schema_name = t.table_schema " +
          "left join INFORMATION_SCHEMA.COLUMNS c on t.table_name = c.table_name and c.TABLE_SCHEMA = s.schema_name " +
          "where s.schema_name not in ('sys', 'SYS', 'mysql', 'information_schema', 'performance_schema', 'MYSQL', 'INFORMATION_SCHEMA', 'PERFORMANCE_SCHEMA')";

  public static final String SQL_MYSQL_FIND_METADATA_BY_SCHEMA =
      " select s.schema_name as schema_name, t.table_name as table_name " +
          ", t.table_comment, c.column_name as column_name, t.table_type " +
          ", c.ordinal_position, c.numeric_precision, c.numeric_scale, c.is_nullable " +
          ", c.data_type, c.column_key, c.character_maximum_length, c.column_comment " +
          "from INFORMATION_SCHEMA.SCHEMATA s " +
          "left join INFORMATION_SCHEMA.TABLES t on s.schema_name = t.table_schema " +
          "left join INFORMATION_SCHEMA.COLUMNS c on t.table_name = c.table_name and c.TABLE_SCHEMA = s.schema_name " +
          "where s.schema_name in (%s)";

  public static final String SQL_MYSQL_FIND_METADATA_BY_TABLE = "";

  public class MySqlColumnLabel {
    public static final String SCHEMA_NAME = "schema_name";
    public static final String TABLE_NAME = "table_name";
    public static final String TABLE_TYPE = "table_type";
    public static final String TABLE_COMMENT = "table_comment";
    public static final String COLUMN_NAME = "column_name";
    public static final String COLUMN_POSITION = "ordinal_position";
    public static final String COLUMN_NUMERIC_PRECISION = "numeric_precision";
    public static final String COLUMN_NUMERIC_SCALE = "numeric_scale";
    public static final String COLUMN_IS_NULLABLE = "is_nullable";
    public static final String COLUMN_PRIMARY_KEY = "column_key";
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_MAX_LENGTH = "character_maximum_length";
    public static final String COLUMN_COMMENT = "column_comment";
  }

  public static final String MYSQL_STATIC_KEY_PRIMARY_KEY = "PRI";

  public static final List<String> MYSQL_NUMERIC_DATA_TYPE
      = Lists.newArrayList("bigint", "bit", "decimal", "double", "float", "int", "mediumint", "smallint", "tinyint");
}
