package pers.jason.etl.metadatamanager.web.rest.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.support.util.TableTypeUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalColumn;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalPlatform;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalSchema;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTable;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType;
import pers.jason.etl.metadatamanager.core.synchronize.external.OracleColumnType;
import pers.jason.etl.metadatamanager.web.common.UserUtil;
import pers.jason.etl.metadatamanager.web.util.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static pers.jason.etl.metadatamanager.core.support.MetadataType.SCHEMA;

/**
 * @author Jason
 * @date 2020/3/15 16:39
 * @description
 */
@Slf4j
@Service
public class OracleMetadataSynchronizeService extends JdbcMetadataSynchronizeService {

  @Override
  protected Platform findDataFromRemote(
      String url, String username, String password, Long platformId, String schemaName, String tableName) {
    Long userId = UserUtil.getUserId();
    String sql = Sql.getSql(PlatformType.ORACLE, schemaName, tableName);
    ExternalPlatform platform = new ExternalPlatform();
    try(
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
      platform.setId(platformId);
      platform.setTypeCode(PlatformType.ORACLE.code);
      platform.setFullName(MetadataUtil.getExternalMetadataFullName(
          MetadataType.PLATFORM, platformId, null, null, null, null
          , null, 0));

      Set<ExternalSchema> schemas = platform.getSchemaSet();

      Map<String, ExternalSchema> schemaMap = Maps.newHashMap();
      Map<String, ExternalTable> tableMap = Maps.newHashMap();
      while(rs.next()) {
        String sName = schemaName;
        String tName = rs.getString(Sql.OracleColumnLabel.TABLE_NAME);
        String tableType = rs.getString(Sql.OracleColumnLabel.TABLE_TYPE);
        String tableComment = rs.getString(Sql.OracleColumnLabel.TABLE_COMMENT);

        String schemaFullName = MetadataUtil.getExternalMetadataFullName(
            SCHEMA, platformId, sName, null, null, null
            , null, 0);
        ExternalSchema schema = schemaMap.get(schemaFullName);
        if(null == schema) {
          schema = new ExternalSchema();
          schema.setPlatformId(platformId);
          schema.setName(sName);
          schema.setCreator(userId);
          schema.setUpdatedBy(userId);
          schema.setFullName(schemaFullName);
          schemas.add(schema);
          schemaMap.put(schemaFullName, schema);
        }

        Set<ExternalTable> tables = schema.getTableSet();
        ExternalTableType externalTableType = TableTypeUtil.getTableType(tableType);
        String tableFullName = MetadataUtil.getExternalMetadataFullName(
            MetadataType.TABLE, platformId, sName, tName, externalTableType, null
            , null, 0);
        ExternalTable table = tableMap.get(tableFullName);
        if(null == table) {
          table = new ExternalTable();
          table.setType(externalTableType.code);
          table.setComments(tableComment);
          table.setFullName(tableFullName);
          table.setName(tName);
          table.setCreator(userId);
          table.setUpdatedBy(userId);
          tables.add(table);
          tableMap.put(tableFullName, table);
        }

        Set<ExternalColumn> columns = table.getColumnSet();
        ExternalColumn column = getExternalColumnFromResultSet(rs, schemaName, platformId);
        column.setCreator(userId);
        column.setUpdatedBy(userId);
        columns.add(column);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return platform;
  }

  private ExternalColumn getExternalColumnFromResultSet(
      final ResultSet rs, String schemaName, Long platformId) throws SQLException {
    String tableName = rs.getString(Sql.OracleColumnLabel.TABLE_NAME);
    String tableType = rs.getString(Sql.OracleColumnLabel.TABLE_TYPE);
    String columnName = rs.getString(Sql.OracleColumnLabel.COLUMN_NAME);
    int columnPosition = rs.getInt(Sql.OracleColumnLabel.COLUMN_POSITION);
    long columnPrecision = rs.getLong(Sql.OracleColumnLabel.COLUMN_NUMERIC_PRECISION);
    long columnScale = rs.getLong(Sql.OracleColumnLabel.COLUMN_NUMERIC_SCALE);
    String columnNullable = rs.getString(Sql.OracleColumnLabel.COLUMN_IS_NULLABLE);
    String columnPrimaryKey = rs.getString(Sql.OracleColumnLabel.COLUMN_PRIMARY_KEY);
    String columnDataType = rs.getString(Sql.OracleColumnLabel.COLUMN_DATA_TYPE);
    long columnMaxLength = rs.getLong(Sql.OracleColumnLabel.COLUMN_MAX_LENGTH);
    String columnComment = rs.getString(Sql.OracleColumnLabel.COLUMN_COMMENT);
    //todo 分区字段
    ExternalColumn column = new ExternalColumn();
    column.setName(columnName);
    column.setPosition(columnPosition);
    column.setComments(columnComment);
    column.setPrimaryKey(Sql.ORACLE_STATIC_KEY_PRIMARY_KEY.equals(columnPrimaryKey));
    column.setNullable(Sql.STATIC_KEY_YES_.equals(columnNullable));
    column.setMaxLength(columnMaxLength);
    column.setNumericScale(columnScale);
    OracleColumnType columnType = OracleColumnType.valueOf(columnDataType.toUpperCase());
    column.setType(columnType.code);
    ExternalTableType externalTableType = TableTypeUtil.getTableType(tableType);
    column.setFullName(MetadataUtil.getExternalMetadataFullName(
        MetadataType.COLUMN, platformId, schemaName, tableName, externalTableType, columnName, columnType, columnPosition));
    return column;
  }
}
