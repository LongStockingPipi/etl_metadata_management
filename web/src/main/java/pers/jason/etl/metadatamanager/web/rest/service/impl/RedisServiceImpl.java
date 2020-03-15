package pers.jason.etl.metadatamanager.web.rest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;
import pers.jason.etl.metadatamanager.core.lock.Distributable;
import pers.jason.etl.metadatamanager.web.props.SimpleProperties;
import pers.jason.etl.metadatamanager.web.rest.service.CacheService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jason
 * @date 2020/2/22 23:05
 * @description
 */
@Slf4j
@Service
public class RedisServiceImpl implements CacheService, Distributable<String> {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private StringRedisSerializer stringRedisSerializer;

  @Autowired
  private ScriptSource lockScript;

  @Autowired
  private ScriptSource unLockScript;

  private SimpleProperties properties;

  @Override
  public <T> Optional<T> getObj(String key) {
    Object obj = null;
    try {
      obj = redisTemplate.boundValueOps(CACHE_KEY_PREFIX + key).get();
      log.debug("get key [" + key + "] value [" + obj + "]");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return Optional.ofNullable((T) obj);
  }

  @Override
  public boolean setObj(String key, Object obj) {
    boolean flag = true;
    try {
      log.debug("save key:{}", key);
      redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + key, obj);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      flag = false;
    }
    return flag;
  }

  /**
   * 放入缓存（带时间） 传入0或负数则不缓存
   *
   * @param key        键
   * @param obj        对象
   * @param expireTime 过期时间
   * @param timeUnit   单位
   * @return
   */
  public boolean setObj(String key, Object obj, long expireTime, TimeUnit timeUnit) {
    if (obj == null) {
      return false;
    }
    boolean flag = true;
    try {
      if (expireTime > 0) {
        redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + key, obj, expireTime, timeUnit);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      flag = false;
    }
    return flag;
  }

  public boolean setHashObj(String key, Map hashMap) {
    if (MapUtils.isEmpty(hashMap)) {
      return false;
    }
    boolean flag = true;
    try {
      redisTemplate.opsForHash().putAll(CACHE_KEY_PREFIX + key, hashMap);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      flag = false;
    }
    return flag;
  }

  /**
   * 获取Hash缓存 多个
   *
   * @param key
   * @param fields
   * @param <T>
   * @return
   */
  public <T> Optional<List<T>> getHashObjects(String key, List<Object> fields) {
    List<T> list = null;
    try {
      list = (List<T>) redisTemplate.boundHashOps(CACHE_KEY_PREFIX + key).multiGet(fields);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return Optional.ofNullable(list);
  }

  /**
   * 删除缓存<br>
   * 根据key精确匹配删除
   *
   * @param key
   */
  public void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(CACHE_KEY_PREFIX + key[0]);
      } else {
        List<String> list = Stream.of(key).map((e) -> CACHE_KEY_PREFIX + e).collect(Collectors.toList());
        redisTemplate.delete(list);
      }
    }
  }

  @Override
  public String getDistributedLock(String lockName) {
    String v = UUID.randomUUID().toString();
    String lockKey = DISTRIBUTED_LOCK_PREFIX + lockName;
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setScriptSource(lockScript);
    redisScript.setResultType(String.class);
    boolean result = false;
    long end = System.currentTimeMillis() + properties.getDistributedLockTimeout();
    while(System.currentTimeMillis() < end && !result) {
      try {
        String res = redisTemplate.execute(redisScript, stringRedisSerializer, stringRedisSerializer
            , Collections.singletonList(lockKey), properties.getLockExpireTime() + "", v);
        result = RESULT_TABLE_SUCCESS.equals(res);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if(result) {
      return v;
    }
    return "";
  }

  @Override
  public void releaseDistributedLock(String lockName, String value) {
    String lockKey = DISTRIBUTED_LOCK_PREFIX + lockName;
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setScriptSource(unLockScript);
    redisScript.setResultType(Long.class);
    try {
      redisTemplate.execute(redisScript, stringRedisSerializer, stringRedisSerializer, Collections.singletonList(lockKey), value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  @Override
//  public String tryGetDistributedLock(String lockName, long timeout, long expireTime) {
//    String v = UUID.randomUUID().toString();
//    long time = System.currentTimeMillis() + timeout;
//    return (String) redisTemplate.execute((RedisCallback) connection -> {
//      String lockKey = DISTRIBUTED_LOCK_PREFIX + lockName;
//      byte[] keyByteArray = lockKey.getBytes();
//      while(System.currentTimeMillis() < time) {
//        if(connection.setNX(keyByteArray, v.getBytes())) {
//          connection.expire(keyByteArray, expireTime);
//          return v;
//        } else if(!(connection.ttl(lockKey.getBytes()) > 0)){
//          connection.expire(keyByteArray, expireTime);
//        } else {
//          try {
//            Thread.sleep(SPIN_CYCLE_TIME);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//            log.error("an attempt to obtain a distributed lock was interrupted!");
//            log.error(e.getMessage(), e);
//          }
//        }
//      }
//      return "";
//    });
//  }

//  @Override
//  public void releaseLock(String lockName, String value) {
//    String lockKey = DISTRIBUTED_LOCK_PREFIX + lockName;
//    byte[] keyByteArray = lockKey.getBytes();
//    redisTemplate.execute((RedisCallback) connection -> {
//      long end = System.currentTimeMillis() + RELEASE_LOCK_TIME_OUT;
//      while(System.currentTimeMillis() < end) {
//        try {
//          connection.watch(keyByteArray);
//          String v = new String(connection.get(keyByteArray));
//          if(v.equals(value)) {
//            connection.multi();
//            connection.del(keyByteArray);
//            connection.exec();
//            return true;
//          }
//          connection.unwatch();
//          break;
//        } catch (Exception e) {
//          log.error(e.getMessage(), e);
//          log.error("other threads have modified the lock information");
//          try {
//            Thread.sleep(SPIN_CYCLE_TIME);
//          } catch (InterruptedException ex) {
//            ex.printStackTrace();
//          }
//        }
//      }
//      return null;
//    });
//  }


}
