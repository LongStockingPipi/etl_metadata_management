package pers.jason.etl.rest.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.rest.pojo.MetadataType;
import pers.jason.etl.rest.pojo.po.Column;
import pers.jason.etl.rest.pojo.po.Metadata;
import pers.jason.etl.rest.pojo.po.Schema;
import pers.jason.etl.rest.pojo.po.Table;
import pers.jason.etl.rest.service.MetadataCrudService;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/21 0:50
 * @description
 */
@Service
public class ExternalMetadataCrudServiceImpl implements MetadataCrudService {

  @Override
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
  public void insertMetadata(Collection<Metadata> metadataList) {
    List<Metadata> insertSchemas = Lists.newArrayList();
    List<Metadata> insertTables = Lists.newArrayList();
    List<Metadata> insertColumns = Lists.newArrayList();
    List<Metadata> missingData = Lists.newArrayList(metadataList);
    Collections.sort(missingData);
    List<String> schemaNames = Lists.newArrayList();
    List<String> tableNames = Lists.newArrayList();
    metadataList.forEach(metadata -> {
      if(MetadataType.SCHEMA.equals(metadata.returnMetadataType())) {
        insertSchemas.add(metadata);
        schemaNames.add(metadata.getFullName());
      } else if(MetadataType.TABLE.equals(metadata.returnMetadataType())) {
        //todo 判断父对象是否被加载过
        if(false) {
          insertTables.add(metadata);
          tableNames.add(metadata.getFullName());
        }
      } else if(MetadataType.COLUMN.equals(metadata.returnMetadataType())) {
        //todo 判断父对象是否被加载过
        if(false) {
          insertColumns.add(metadata);
        }
      }
    });

    //todo：单独新增的的表或字段，通过目前的实现方式无法获得父ID




  }

  @Override
  public void deleteSchemaById(List<Long> ids) {

  }

  @Override
  public void deleteTableById(List<Long> ids) {

  }

  @Override
  public void deleteColumnById(List<Long> ids) {

  }

  @Override
  public List<Schema> insertSchema(List<Schema> schemas) {
    return null;
  }

  @Override
  public List<Table> insertTables(List<Table> tables) {
    return null;
  }

  @Override
  public List<Column> insertColumns(List<Column> columns) {
    return null;
  }
}
