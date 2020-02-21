package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.jason.etl.rest.pojo.po.ExternalSchema;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/17 23:32
 * @description
 */
@Mapper
public interface ExternalSchemaDao {

  void saveAll(List<ExternalSchema> schemas);

  void delete(@Param("ids") List<Long> ids);

  ExternalSchema findSchema(Long id);
}
