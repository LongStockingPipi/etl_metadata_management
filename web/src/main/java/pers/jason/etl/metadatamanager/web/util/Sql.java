package pers.jason.etl.metadatamanager.web.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import pers.jason.etl.metadatamanager.core.support.PlatformType;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/22 23:10
 * @description
 */
public class Sql {

  public static final String EMPTY_STRING = "";

  public static final String STATIC_KEY_YES = "YES";

  public static final String STATIC_KEY_NO = "NO";

  public static final String STATIC_KEY_YES_ = "Y";

  public static final String STATIC_KEY_NO_ = "N";

  public static String getSql(PlatformType platformType, String schemaName, String tableName) {

    if(PlatformType.MYSQL.equals(platformType)) {
      if(StringUtils.isEmpty(schemaName)) {
        return Sql.SQL_MYSQL_FIND_METADATA_BY_PLATFORM;
      } else {
        if(StringUtils.isEmpty(tableName)) {
          return String.format(Sql.SQL_MYSQL_FIND_METADATA_BY_SCHEMA, "'" + schemaName + "'");
        } else {
          return Sql.SQL_MYSQL_FIND_METADATA_BY_TABLE;
        }
      }
    }

    if(PlatformType.ORACLE.equals(platformType)) {
      if(StringUtils.isEmpty(schemaName)) {
        return EMPTY_STRING;
      } else {
        if(StringUtils.isEmpty(tableName)) {
          return String.format(Sql.SQL_ORACLE_FIND_METADATA_BY_SCHEMA, schemaName, schemaName, schemaName);
        } else {
          return Sql.SQL_ORACLE_FIND_METADATA_BY_TABLE;
        }
      }
    }

    return EMPTY_STRING;
  }

  public static final String SQL_ORACLE_FIND_METADATA_BY_PLATFORM = "";

  public static final String SQL_ORACLE_FIND_METADATA_BY_SCHEMA =
      " select tb.name as table_name, tb.type as table_type, tb.COMMENTS as table_comment, c.COLUMN_NAME as column_name" +
          "     , c.DATA_TYPE as column_type, c.DATA_LENGTH as column_length, c.DATA_PRECISION as column_precision" +
          "     , c.DATA_SCALE as column_scale, c.NULLABLE as column_nullable, c.COLUMN_ID as column_position" +
          "     , c.COMMENTS as column_comment, c.CONSTRAINT_TYPE as column_is_pk" +
          " from ( " + //查询表（视图）
          "   select tb_.name, tb_.type, atc.COMMENTS from (" +
          "        select TABLE_NAME as name, 'TABLE' as type, OWNER FROM ALL_TABLES where OWNER = '%s'" +
          "        union" +
          "        select VIEW_NAME as name, 'VIEW' as type, OWNER FROM ALL_VIEWS where OWNER = '%s'" +
          "    ) tb_ inner join ALL_TAB_COMMENTS atc on atc.OWNER = tb_.OWNER and tb_.name = atc.TABLE_NAME" +
          " ) tb inner join ( " + //查询字段
          "    select" +
          "           atc.TABLE_NAME, atc.COLUMN_NAME, atc.DATA_TYPE, atc.DATA_LENGTH, atc.DATA_PRECISION" +
          "         , atc.DATA_SCALE, atc.NULLABLE, atc.COLUMN_ID, acc.COMMENTS, upk.CONSTRAINT_TYPE" +
          "    FROM ALL_TAB_COLUMNS atc" +
          "    INNER JOIN ALL_COL_COMMENTS acc on atc.TABLE_NAME = acc.TABLE_NAME and atc.COLUMN_NAME = acc.COLUMN_NAME" +
          "    LEFT JOIN  ( " + //用户范围的约束信息
          "        select us.OWNER, us.TABLE_NAME, ucc.COLUMN_NAME, us.CONSTRAINT_TYPE from USER_CONSTRAINTS us" +
          "        inner join USER_CONS_COLUMNS ucc" +
          "        on us.TABLE_NAME = ucc.TABLE_NAME and us.CONSTRAINT_NAME = ucc.CONSTRAINT_NAME" +
          "        where us.CONSTRAINT_TYPE = 'P'" +
          "    ) upk on atc.TABLE_NAME = upk.TABLE_NAME and atc.COLUMN_NAME = upk.COLUMN_NAME" +
          "    WHERE atc.OWNER = '%s'" +
          " ) c on c.TABLE_NAME = tb.name order by table_name, column_position";

  public static final String SQL_ORACLE_FIND_METADATA_BY_TABLE = "";

  public static final String SQL_MYSQL_FIND_METADATA_BY_PLATFORM =
      " select s.schema_name as schema_name, t.table_name as table_name " +
          ", t.table_comment, c.column_name as column_name, t.table_type " +
          ", c.ordinal_position, c.numeric_precision, c.numeric_scale, c.is_nullable " +
          ", c.data_type, c.column_key, c.character_maximum_length, c.column_comment " +
          "from INFORMATION_SCHEMA.SCHEMATA s " +
          "left join INFORMATION_SCHEMA.TABLES t on s.schema_name = t.table_schema " +
          "left join INFORMATION_SCHEMA.COLUMNS c on t.table_name = c.table_name and c.TABLE_SCHEMA = s.schema_name " +
          "where s.schema_name not in ('sys', 'SYS', 'mysql', 'information_schema', 'performance_schema', 'MYSQL' " +
          ", 'INFORMATION_SCHEMA', 'PERFORMANCE_SCHEMA')";

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

  public class OracleColumnLabel {
    public static final String TABLE_NAME = "table_name";
    public static final String TABLE_TYPE = "table_type";
    public static final String TABLE_COMMENT = "table_comment";
    public static final String COLUMN_NAME = "column_name";
    public static final String COLUMN_POSITION = "column_position";
    public static final String COLUMN_NUMERIC_PRECISION = "column_precision";
    public static final String COLUMN_NUMERIC_SCALE = "column_scale";
    public static final String COLUMN_IS_NULLABLE = "column_nullable";
    public static final String COLUMN_PRIMARY_KEY = "column_is_pk";
    public static final String COLUMN_DATA_TYPE = "column_type";
    public static final String COLUMN_MAX_LENGTH = "column_length";
    public static final String COLUMN_COMMENT = "column_comment";
  }

  public static final String MYSQL_STATIC_KEY_PRIMARY_KEY = "PRI";

  public static final String ORACLE_STATIC_KEY_PRIMARY_KEY = "P";

  public static final List<String> MYSQL_NUMERIC_DATA_TYPE
      = Lists.newArrayList("bigint", "bit", "decimal", "double", "float", "int", "mediumint", "smallint", "tinyint");
}
