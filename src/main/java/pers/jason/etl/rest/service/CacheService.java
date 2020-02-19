package pers.jason.etl.rest.service;

import java.util.Optional;

/**
 * @author Jason
 * @date 2020/2/19 18:50
 * @description
 */
public interface CacheService {

  String KEY_PREFIX = "ETL_MD_";

  <T> Optional<T> getObj(String key);

  boolean setObj(String key, Object obj);
}
