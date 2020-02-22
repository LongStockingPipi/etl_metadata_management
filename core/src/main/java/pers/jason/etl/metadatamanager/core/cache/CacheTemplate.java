package pers.jason.etl.metadatamanager.core.cache;

import java.util.Optional;

/**
 * @author Jason
 * @date 2020/2/22 23:04
 * @description
 */
public interface CacheTemplate {

  <T> Optional<T> getObj(String key);

  boolean setObj(String key, Object obj);
}
