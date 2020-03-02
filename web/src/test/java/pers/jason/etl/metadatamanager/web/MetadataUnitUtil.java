package pers.jason.etl.metadatamanager.web;

import org.assertj.core.util.Sets;
import pers.jason.etl.metadatamanager.core.support.Symbol;
import pers.jason.etl.metadatamanager.core.synchronize.Column;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalColumn;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalSchema;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTable;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType;
import pers.jason.etl.metadatamanager.core.synchronize.external.MySqlColumnType;

import java.util.Date;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/3/2 9:43
 * @description
 */
public class MetadataUnitUtil {

  public static Set<ExternalSchema> getSchema(String prefix, Long platformId, int size, Long beginId) {
    Set<ExternalSchema> schemas = Sets.newHashSet();
    Long tableId = 1L;
    for (int i = 0; i < size; i++) {
      ExternalSchema schema = new ExternalSchema();
      String schemaName = platformId + "_sch_name_" + i;
      schema.setId(beginId);
      schema.setName(schemaName);
      schema.setParentId(platformId);
      schema.setCreator(1L);
      schema.setUpdatedBy(1L);
      schema.setUpdatedTime(new Date());
      schema.setCreatedTime(new Date());
      schema.setFullName(prefix + Symbol.SIGN_SLASH + schemaName);
      schema.setTableSet(getTable(schema.getFullName(), schema.getId(), 40, tableId));
      schemas.add(schema);
      tableId ++;
      beginId++;
    }
    return schemas;
  }

  public static Set<ExternalTable> getTable(String prefix, Long schemaId, int size, Long beginId) {
    Set<ExternalTable> tables = Sets.newHashSet();
    Long columnId = 1L;
    for (int i = 0; i < size; i++) {
      ExternalTable table = new ExternalTable();
      String tableName = schemaId + "_tal_name_" + i;
      table.setId(beginId);
      table.setName(tableName);
      table.setParentId(schemaId);
      table.setType(ExternalTableType.BASE_TABLE.code);
      table.setWritable(true);
      table.setCreator(1L);
      table.setUpdatedBy(1L);
      table.setUpdatedTime(new Date());
      table.setCreatedTime(new Date());
      table.setFullName(prefix + Symbol.SIGN_SLASH + tableName + Symbol.SIGN_UNDERLINE + ExternalTableType.BASE_TABLE.code);
      table.setColumnSet(getColumn(table.getFullName(), table.getId(), 20, columnId));
      tables.add(table);
      columnId++;
      beginId ++;
    }
    return tables;
  }

  public static Set<ExternalColumn> getColumn(String prefix, Long tableId, int size, Long beginId) {
    Set<ExternalColumn> columns = Sets.newHashSet();
    for (int i = 0; i < size; i++) {
      ExternalColumn column = new ExternalColumn();
      String columnName = tableId + "_col_name_" + i;
      column.setId(beginId);
      column.setName(columnName);
      column.setTableId(tableId);
      column.setMaxLength(256L);
      column.setNullable(false);
      column.setType(MySqlColumnType.VARCHAR.code);
      column.setNumericScale(0L);
      column.setPartitionField(false);
      column.setPrimaryKey(false);
      column.setPosition(i);
      column.setCreator(1L);
      column.setUpdatedBy(1L);
      column.setUpdatedTime(new Date());
      column.setCreatedTime(new Date());
      column.setFullName(prefix + Symbol.SIGN_SLASH + columnName + Symbol.SIGN_UNDERLINE + MySqlColumnType.VARCHAR.code + Symbol.SIGN_UNDERLINE + i);
      columns.add(column);
      beginId ++;
    }
    return columns;
  }

  public static void main(String[] args) {
//    Set<ExternalColumn> columns = getColumn("/a/b/c", 1L, 20, 1L);
//    columns.forEach(column -> {
//      System.out.println(column.toString());
//    });
  }
}
