package pers.jason.etl.rest.service;

import pers.jason.etl.rest.pojo.po.Column;
import pers.jason.etl.rest.pojo.po.Metadata;
import pers.jason.etl.rest.pojo.po.Schema;
import pers.jason.etl.rest.pojo.po.Table;

import java.util.Collection;
import java.util.List;

/**
 * @author Jason
 * @date 2020/2/21 0:49
 * @description
 */
public interface MetadataCrudService {

  Integer MAX_INSERT_COUNT = 500;

  void deleteMetadata(Collection<Metadata> metadataList);

  void insertMetadata(Collection<Metadata> metadataList);

  void deleteSchemaById(List<Long> ids);

  void deleteTableById(List<Long> ids);

  void deleteColumnById(List<Long> ids);

  <T extends Schema> void insertSchema(List<T> schemas);

  <T extends Table> void insertTables(List<T> tables);

  <T extends Column> void insertColumns(List<T> columns);
}
