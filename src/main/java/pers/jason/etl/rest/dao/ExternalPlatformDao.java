package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.jason.etl.rest.pojo.po.ExternalPlatform;

/**
 * @author Jason
 * @date 2020/2/17 23:31
 * @description
 */
@Mapper
public interface ExternalPlatformDao {

  ExternalPlatform save(ExternalPlatform platform);

  void delete(Long id);

  /**
   *
   * @param platformId not null
   * @param schemaId
   * @param tableId
   * @return
   */
  ExternalPlatform findAll(Long platformId, Long schemaId, Long tableId);

  ExternalPlatform findPlatform(Long platformId);
}
