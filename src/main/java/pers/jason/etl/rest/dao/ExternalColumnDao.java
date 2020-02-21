package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.jason.etl.rest.pojo.po.ExternalColumn;
import pers.jason.etl.rest.pojo.po.ExternalTable;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/18 0:29
 * @description
 */
@Mapper
public interface ExternalColumnDao {

  void delete(@Param("ids") List<Long> ids);

  void saveAll(List<ExternalColumn> columns);
}
