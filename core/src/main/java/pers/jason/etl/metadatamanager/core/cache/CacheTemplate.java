package pers.jason.etl.metadatamanager.core.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Jason
 * @date 2020/2/22 23:04
 * @description
 */
public interface CacheTemplate {

  <T> Optional<T> getObj(String key);

  boolean setObj(String key, Object obj);

  boolean setObj(String key, Object obj, long expireTime, TimeUnit timeUnit);

  <T> Optional<List<T>> getHashObjects(String key, List<Object> fields);

  boolean setHashObj(String key, Map hashMap);

  void del(String... key);
}
