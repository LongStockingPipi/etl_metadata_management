package pers.jason.etl.metadatamanager.web.rest.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.synchronize.Column;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Schema;
import pers.jason.etl.metadatamanager.core.synchronize.Table;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalColumn;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalSchema;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTable;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalColumnDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalPlatformDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalSchemaDao;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalTableDao;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataCrudService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/21 0:50
 * @description
 */
@Service
public class ExternalMetadataServiceImpl implements MetadataCrudService {

  @Autowired
  private ExternalPlatformDao platformDao;

  @Autowired
  private ExternalSchemaDao schemaDao;

  @Autowired
  private ExternalTableDao tableDao;

  @Autowired
  private ExternalColumnDao columnDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteMetadata(Collection<Metadata> metadataList) {
    if(!CollectionUtils.isEmpty(metadataList)) {
      Set<Long> schemaIds = Sets.newHashSet();
      Set<Long> tableIds = Sets.newHashSet();
      Set<Long> columnIds = Sets.newHashSet();
      Iterator<Metadata> iterator = metadataList.iterator();
      while(iterator.hasNext()) {
        Metadata metadata = iterator.next();
        if(MetadataType.SCHEMA.equals(metadata.returnMetadataType())) {
          schemaIds.add(metadata.getId());
        } else if(MetadataType.TABLE.equals(metadata.returnMetadataType())) {
          tableIds.add(metadata.getId());
        } else if(MetadataType.COLUMN.equals(metadata.returnMetadataType())) {
          columnIds.add(metadata.getId());
        }
      }
      if(!CollectionUtils.isEmpty(schemaIds)) {
        deleteSchemaById(Lists.newArrayList(schemaIds));
      }
      if(!CollectionUtils.isEmpty(tableIds)) {
        deleteTableById(Lists.newArrayList(tableIds));
      }
      if(!CollectionUtils.isEmpty(columnIds)) {
        deleteColumnById(Lists.newArrayList(columnIds));
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void insertMetadata(Collection<Metadata> metadataList) {
    if(!CollectionUtils.isEmpty(metadataList)) {
      List<Schema> schemas = Lists.newArrayList();
      List<Table> tables = Lists.newArrayList();
      List<Column> columns = Lists.newArrayList();
      metadataList.forEach(metadata -> {
        MetadataType metadataType = metadata.returnMetadataType();
        if(MetadataType.SCHEMA.equals(metadataType)) {
          schemas.add((Schema) metadata);
        } else if(MetadataType.TABLE.equals(metadataType)) {
          tables.add((Table) metadata);
        } else if(MetadataType.COLUMN.equals(metadataType)) {
          columns.add((Column) metadata);
        }
      });

      insertSchema(schemas);
      schemas.forEach(schema -> {
        Set<Metadata> child = schema.getChild();
        if(!CollectionUtils.isEmpty(child)) {
          child.forEach(table -> {
            table.setParentId(schema.getId());
            tables.add((Table) table);
          });
        }
      });

      insertTables(tables);
      tables.forEach(table -> {
        Set<Metadata> child = table.getChild();
        if(!CollectionUtils.isEmpty(child)) {
          child.forEach(column -> {
            column.setParentId(table.getId());
            columns.add((Column) column);
          });
        }
      });

      insertColumns(columns);
    }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteSchemaById(List<Long> ids) {
    schemaDao.delete(ids);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTableById(List<Long> ids) {
    tableDao.delete(ids);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteColumnById(List<Long> ids) {
    columnDao.delete(ids);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public <T extends Schema> void insertSchema(List<T> schemas) {
    if(!CollectionUtils.isEmpty(schemas)) {
      List<ExternalSchema> externalSchemas = (List<ExternalSchema>) schemas;
      List<List<ExternalSchema>> schemaGroups = Lists.partition(externalSchemas, MAX_INSERT_COUNT);
      for(List<ExternalSchema> group : schemaGroups) {
        schemaDao.saveAll(group);
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public <T extends Table> void insertTables(List<T> tables) {
    if(!CollectionUtils.isEmpty(tables)) {
      List<ExternalTable> externalTables = (List<ExternalTable>) tables;
      List<List<ExternalTable>> tableGroups = Lists.partition(externalTables, MAX_INSERT_COUNT);
      for(List<ExternalTable> group : tableGroups) {
        tableDao.saveAll(group);
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public <T extends Column> void insertColumns(List<T> columns) {
    if(!CollectionUtils.isEmpty(columns)) {
      List<ExternalColumn> externalColumns = (List<ExternalColumn>) columns;
      List<List<ExternalColumn>> columnGroups = Lists.partition(externalColumns, MAX_INSERT_COUNT);
      for(List<ExternalColumn> group : columnGroups) {
        try {
          columnDao.saveAll(group);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }


}
