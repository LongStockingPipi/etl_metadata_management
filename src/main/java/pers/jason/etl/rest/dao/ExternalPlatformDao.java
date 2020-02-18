package pers.jason.etl.rest.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.jason.etl.rest.pojo.po.ExternalPlatform;

import java.util.List;

/**
 * @author Jason
 * @date 2020/2/17 23:31
 * @description
 */
@Mapper
public interface ExternalPlatformDao {

  ExternalPlatform save(ExternalPlatform platform);

  ExternalPlatform saveAll(List<ExternalPlatform> platforms);

  void delete(Long id);

  /**
   *
   * @param platformId not null
   * @param schemaId
   * @param tableId
   * @return
   */
  ExternalPlatform findAll(@Param("platformId") Long platformId, @Param("schemaId") Long schemaId, @Param("tableId") Long tableId);

  ExternalPlatform findPlatform(@Param("id") Long platformId);
}
