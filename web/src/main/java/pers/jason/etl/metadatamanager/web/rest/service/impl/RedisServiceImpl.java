package pers.jason.etl.metadatamanager.web.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pers.jason.etl.metadatamanager.core.cache.CacheTemplate;

import java.util.Optional;

/**
 * @author Jason
 * @date 2020/2/22 23:05
 * @description
 */
@Service
public class RedisServiceImpl implements CacheTemplate {

  private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

  private static final String KEY_PREFIX = "ETL_MD_";

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Override
  public <T> Optional<T> getObj(String key) {
    Object obj = null;
    try {
      obj = redisTemplate.boundValueOps(KEY_PREFIX + key).get();
      logger.debug("从redis获取key为{}的缓存成功", key);
      logger.debug("缓存内容为{}", obj);
    } catch (Exception e) {
      logger.info("从redis获取缓存信息失败");
      logger.error(e.getMessage(), e);
    }
    return Optional.ofNullable((T) obj);
  }

  @Override
  public boolean setObj(String key, Object obj) {
    boolean flag = true;
    try {
      logger.debug("redis放入缓存（永久）,key:{}", key);
      redisTemplate.opsForValue().set(KEY_PREFIX + key, obj);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      flag = false;
    }
    return flag;
  }
}
