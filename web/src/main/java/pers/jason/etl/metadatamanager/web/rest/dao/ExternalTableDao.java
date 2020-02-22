package pers.jason.etl.metadatamanager.web.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTable;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/18 0:29
 * @description
 */
@Mapper
public interface ExternalTableDao {

  void delete(@Param("ids") List<Long> ids);

  void saveAll(List<ExternalTable> tables);

  //todo
  ExternalTable findTable(Long id);
}
