package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.jason.etl.rest.pojo.po.ExternalTable;

/**
 * @author Jason
 * @date 2020/2/18 0:29
 * @description
 */
@Mapper
public interface ExternalTableDao {

  ExternalTable save(ExternalTable table);

  void delete(Long id);

  ExternalTable findTable(Long id);
}
