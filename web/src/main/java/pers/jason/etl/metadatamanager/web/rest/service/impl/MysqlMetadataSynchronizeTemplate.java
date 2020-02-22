package pers.jason.etl.metadatamanager.web.rest.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.metadatamanager.core.cache.CacheTemplate;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.support.util.TableTypeUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalColumn;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalPlatform;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalSchema;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTable;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType;
import pers.jason.etl.metadatamanager.core.synchronize.external.MySqlColumnType;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.web.common.UserUtil;
import pers.jason.etl.metadatamanager.web.exception.PlatformNotFoundException;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalPlatformDao;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataCrudService;
import pers.jason.etl.metadatamanager.web.rest.service.SynchronizeServiceHolder;
import pers.jason.etl.metadatamanager.web.util.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static pers.jason.etl.metadatamanager.core.support.MetadataType.SCHEMA;

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
  private CacheTemplate cacheService;

  @Autowired
  private SynchronizeServiceHolder synchronizeServiceHolder;

  @Override
  protected Platform findDataFromLocal(Long platformId, Long schemaId, Long tableId) {
//    Platform platform = (Platform) cacheService.getObj("")
//        .orElse(platformDao.findAll(platformId, schemaId, tableId));
    Platform platform = platformDao.findAll(platformId, schemaId, tableId);
    if(null == platform) {
      throw new PlatformNotFoundException("the platform information is not available locally");
    }
    return platform;
  }

  @Override
  protected Platform findDataFromRemote(
      String url, String username, String password, Long platformId, String schemaName, String tableName) {
    Long userId = UserUtil.getUserId();
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
        String sName = rs.getString(Sql.MySqlColumnLabel.SCHEMA_NAME);
        String tName = rs.getString(Sql.MySqlColumnLabel.TABLE_NAME);
        String tableType = rs.getString(Sql.MySqlColumnLabel.TABLE_TYPE);
        String tableComment = rs.getString(Sql.MySqlColumnLabel.TABLE_COMMENT);

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
        ExternalColumn column = getExternalColumnFromResultSet(rs, platformId);
        column.setCreator(userId);
        column.setUpdatedBy(userId);
        columns.add(column);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return platform;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  protected void processingData(Platform localData, Map<String, Set<Metadata>> discrepantData) {
    MetadataCrudService metadataCrudService = synchronizeServiceHolder.findMetadataCrudService(PlatformType.MYSQL);
    Set<Metadata> mis = discrepantData.get(DATA_MISSING);
    Set<Metadata> ref = discrepantData.get(DATA_REFUND);
    metadataCrudService.deleteMetadata(ref);

    if(!CollectionUtils.isEmpty(mis)) {
      List<Metadata> missingData =
          removeRedundancyMetadataAndSetParentId(localData, Lists.newArrayList(mis));
      metadataCrudService.insertMetadata(missingData);
    }

    //todo 更新缓存
  }

  /**
   * 去除重复数据
   * 为新数据添加父ID
   * 时间复杂度(n*m)
   * @param localData
   * @param missingData
   * @return
   */
  private List<Metadata> removeRedundancyMetadataAndSetParentId(Platform localData, List<Metadata> missingData) {
    //排序，保证元数据顺序是p-s-t-c
    Collections.sort(missingData);

    //准备已存在的元数据id映射关系
    Map<String, Long> fullNameAndId = Maps.newHashMap();
    registerFullNameAndIdInMap(localData, fullNameAndId);

    //去重后的fullName集合
    List<String> names = Lists.newArrayList();
    missingData.forEach(metadata -> {
      names.add(metadata.getFullName());
    });
    //去重后的metadata集合
    List<Metadata> newMetadata = Lists.newArrayList();
    missingData.forEach(metadata -> {
      if(null != metadata && null == metadata.getId()) {
        String fullName = metadata.getFullName();
        String parentFullName = MetadataUtil.getParentFullName(fullName);
        if(!names.contains(parentFullName)) { //断层插入
          if(!SCHEMA.equals(metadata.returnMetadataType())) {
            if(null == metadata.getId()) { //为新数据查询父ID
              metadata.setParentId(fullNameAndId.get(parentFullName));
            }
          }
          newMetadata.add(metadata);
        }
      }
    });
    return newMetadata;
  }



  private void registerFullNameAndIdInMap(Metadata metadata, Map<String, Long> map) {
    Set<Metadata> child = metadata.getChild();
    if(!CollectionUtils.isEmpty(child)) {
      for(Metadata data : child) {
        registerFullNameAndIdInMap(data, map);
      }
    }

    map.put(metadata.getFullName(), metadata.getId());
  }

  private ExternalColumn getExternalColumnFromResultSet(final ResultSet rs, Long platformId) throws SQLException {
    String schemaName = rs.getString(Sql.MySqlColumnLabel.SCHEMA_NAME);
    String tableName = rs.getString(Sql.MySqlColumnLabel.TABLE_NAME);
    String tableType = rs.getString(Sql.MySqlColumnLabel.TABLE_TYPE);
    String columnName = rs.getString(Sql.MySqlColumnLabel.COLUMN_NAME);
    int columnPosition = rs.getInt(Sql.MySqlColumnLabel.COLUMN_POSITION);
    long columnPrecision = rs.getLong(Sql.MySqlColumnLabel.COLUMN_NUMERIC_PRECISION);
    long columnScale = rs.getLong(Sql.MySqlColumnLabel.COLUMN_NUMERIC_SCALE);
    String columnNullable = rs.getString(Sql.MySqlColumnLabel.COLUMN_IS_NULLABLE);
    String columnPrimaryKey = rs.getString(Sql.MySqlColumnLabel.COLUMN_PRIMARY_KEY);
    String columnDataType = rs.getString(Sql.MySqlColumnLabel.COLUMN_DATA_TYPE);
    long columnMaxLength = rs.getLong(Sql.MySqlColumnLabel.COLUMN_MAX_LENGTH);
    String columnComment = rs.getString(Sql.MySqlColumnLabel.COLUMN_COMMENT);

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
