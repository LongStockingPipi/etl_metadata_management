package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.jason.etl.rest.pojo.po.ExternalColumn;

/**
 * @author Jason
 * @date 2020/2/18 0:29
 * @description
 */
@Mapper
public interface ExternalColumnDao {

  ExternalColumn save(ExternalColumn column);

  void delete(Long id);
}
