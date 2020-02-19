package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.jason.etl.rest.pojo.po.ExternalSchema;

/**
 * @author Jason
 * @date 2020/2/17 23:32
 * @description
 */
@Mapper
public interface ExternalSchemaDao {

//  ExternalSchema save(ExternalSchema schema);
//
//  void delete(Long id);

  ExternalSchema findSchema(Long id);
}
