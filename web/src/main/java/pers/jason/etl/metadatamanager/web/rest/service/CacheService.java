package pers.jason.etl.metadatamanager.web.rest.service;

import pers.jason.etl.metadatamanager.core.cache.CacheTemplate;

/**
 * @author Jason
 * @date 2020/2/28 18:40
 * @description
 */
public interface CacheService extends CacheTemplate {

  String CACHE_KEY_PREFIX = "etl:metadata:";

  String DISTRIBUTED_LOCK_PREFIX = "etl:metadata:distributed:lock:";

  Long RELEASE_LOCK_TIME_OUT = 5000L;

  Long SPIN_CYCLE_TIME = 100L;

  String RESULT_TABLE_SUCCESS = "OK";

  String RESULT_TABLE_FAILED = "ERR";

}
