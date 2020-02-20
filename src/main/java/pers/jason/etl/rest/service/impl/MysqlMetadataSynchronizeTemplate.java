package pers.jason.etl.rest.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.rest.dao.ExternalPlatformDao;
import pers.jason.etl.rest.exception.PlatformNotFoundException;
import pers.jason.etl.rest.pojo.ExternalTableType;
import pers.jason.etl.rest.pojo.MetadataType;
import pers.jason.etl.rest.pojo.MySqlColumnType;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.pojo.po.ExternalColumn;
import pers.jason.etl.rest.pojo.po.ExternalPlatform;
import pers.jason.etl.rest.pojo.po.ExternalSchema;
import pers.jason.etl.rest.pojo.po.ExternalTable;
import pers.jason.etl.rest.pojo.po.Metadata;
import pers.jason.etl.rest.pojo.po.Platform;
import pers.jason.etl.rest.service.CacheService;
import pers.jason.etl.rest.service.MetadataCrudService;
import pers.jason.etl.rest.service.MetadataSynchronizeTemplate;
import pers.jason.etl.rest.utils.MetadataUtil;
import pers.jason.etl.rest.utils.Sql;
import pers.jason.etl.rest.utils.Sql.MySqlColumnLabel;
import pers.jason.etl.rest.utils.TableTypeUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/18 22:16
 * @description
 */
@Service
public class MysqlMetadataSynchronizeTemplate extends MetadataSynchronizeTemplate {

  @Autowired
  private ExternalPlatformDao platformDao;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private SynchronizeServiceHolder synchronizeServiceHolder;

  @Override
  protected Platform findDataFromLocal(Long platformId, Long schemaId, Long tableId) {
    Platform platform = (Platform) cacheService.getObj("")
        .orElse(platformDao.findAll(platformId, schemaId, tableId));
    if(null == platform) {
      throw new PlatformNotFoundException("the platform information is not available locally");
    }
    return platform;
  }

  @Override
  protected Platform findDataFromRemote(
      String url, String username, String password, Long platformId, String schemaName, String tableName) {
    String sql = getSql(schemaName, tableName);
    ExternalPlatform platform = new ExternalPlatform();
    try(
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {


      platform.setId(platformId);
      platform.setTypeCode(PlatformType.MYSQL.code);
      platform.setFullName(MetadataUtil.getExternalMetadataFullName(
          MetadataType.PLATFORM, platformId, null, null, null, null
          , null, 0));

      Set<ExternalSchema> schemas = platform.getSchemaSet();

      Map<String, ExternalSchema> schemaMap = Maps.newHashMap();
      Map<String, ExternalTable> tableMap = Maps.newHashMap();
      while(rs.next()) {
        String sName = rs.getString(MySqlColumnLabel.SCHEMA_NAME);
        String tName = rs.getString(MySqlColumnLabel.TABLE_NAME);
        String tableType = rs.getString(MySqlColumnLabel.TABLE_TYPE);
        String tableComment = rs.getString(MySqlColumnLabel.TABLE_COMMENT);

        String schemaFullName = MetadataUtil.getExternalMetadataFullName(
            MetadataType.SCHEMA, platformId, sName, null, null, null
            , null, 0);
        ExternalSchema schema = schemaMap.get(schemaFullName);
        if(null == schema) {
          schema = new ExternalSchema();
          schema.setPlatformId(platformId);
          schema.setName(sName);
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
          tables.add(table);
          tableMap.put(tableFullName, table);
        }

        Set<ExternalColumn> columns = table.getColumnSet();
        ExternalColumn column = getExternalColumnFromResultSet(rs, platformId);
        columns.add(column);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return platform;
  }

  @Override
  protected void processingData(Map<String, Set<Metadata>> discrepantData) {
    MetadataCrudService metadataCrudService = synchronizeServiceHolder.findMetadataCrudService(PlatformType.MYSQL);
    Set<Metadata> mis = discrepantData.get(DATA_MISSING);
    Set<Metadata> ref = discrepantData.get(DATA_REFUND);
    metadataCrudService.deleteMetadata(ref);
    metadataCrudService.insertMetadata(mis);
    //todo 更新缓存
  }

  private ExternalColumn getExternalColumnFromResultSet(final ResultSet rs, Long platformId) throws SQLException {
    String schemaName = rs.getString(MySqlColumnLabel.SCHEMA_NAME);
    String tableName = rs.getString(MySqlColumnLabel.TABLE_NAME);
    String tableType = rs.getString(MySqlColumnLabel.TABLE_TYPE);
    String columnName = rs.getString(MySqlColumnLabel.COLUMN_NAME);
    int columnPosition = rs.getInt(MySqlColumnLabel.COLUMN_POSITION);
    long columnPrecision = rs.getLong(MySqlColumnLabel.COLUMN_NUMERIC_PRECISION);
    long columnScale = rs.getLong(MySqlColumnLabel.COLUMN_NUMERIC_SCALE);
    String columnNullable = rs.getString(MySqlColumnLabel.COLUMN_IS_NULLABLE);
    String columnPrimaryKey = rs.getString(MySqlColumnLabel.COLUMN_PRIMARY_KEY);
    String columnDataType = rs.getString(MySqlColumnLabel.COLUMN_DATA_TYPE);
    long columnMaxLength = rs.getLong(MySqlColumnLabel.COLUMN_MAX_LENGTH);
    String columnComment = rs.getString(MySqlColumnLabel.COLUMN_COMMENT);

    ExternalColumn column = new ExternalColumn();
    column.setName(columnName);
    column.setPosition(columnPosition);
    column.setComments(columnComment);
    column.setPrimaryKey(Sql.MYSQL_STATIC_KEY_PRIMARY_KEY.equals(columnPrimaryKey));
    column.setNullable(Sql.STATIC_KEY_YES.equals(columnNullable));
    column.setMaxLength(Sql.MYSQL_NUMERIC_DATA_TYPE.contains(columnDataType) ? columnPrecision : columnMaxLength);
    column.setNumericScale(columnScale);
    MySqlColumnType columnType = MySqlColumnType.valueOf(columnDataType.toUpperCase());
    column.setType(columnType.code);
    ExternalTableType externalTableType = TableTypeUtil.getTableType(tableType);
    column.setFullName(MetadataUtil.getExternalMetadataFullName(
        MetadataType.COLUMN, platformId, schemaName, tableName, externalTableType, columnName, columnType, columnPosition));
    return column;
  }

  private String getSql(String schemaName, String tableName) {
    if(StringUtils.isEmpty(schemaName)) {
      return Sql.SQL_MYSQL_FIND_METADATA_BY_PLATFORM;
    } else {
      if(StringUtils.isEmpty(tableName)) {
        return Sql.SQL_MYSQL_FIND_METADATA_BY_SCHEMA;
      } else {
        return Sql.SQL_MYSQL_FIND_METADATA_BY_TABLE;
      }
    }
  }
}
