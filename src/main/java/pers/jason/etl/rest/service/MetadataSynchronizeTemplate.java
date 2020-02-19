package pers.jason.etl.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.jason.etl.rest.exception.RequestTimeoutException;
import pers.jason.etl.rest.pojo.SynchronizeModel;
import pers.jason.etl.rest.pojo.ThreadLock;
import pers.jason.etl.rest.pojo.po.Metadata;
import pers.jason.etl.rest.pojo.po.Platform;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jason
 * @date 2020/2/18 0:51
 * @description
 */
public abstract class MetadataSynchronizeTemplate {

  private static final Logger logger = LoggerFactory.getLogger(MetadataSynchronizeTemplate.class);

  private static final String DATA_MISSING = "missingData";

  private static final String DATA_REFUND = "refundData";

  private static Lock holderLock = new ReentrantLock();

  private static ThreadLocal<Map<String, ThreadLock>> connectionHolder = new ThreadLocal<>();

  public final void synchronize(SynchronizeModel synchronizeModel)
      throws InterruptedException {
//    String threadName = Thread.currentThread().getName();
//    logger.info(threadName + "进入同步方法");
//    //抢锁
//    ThreadLock lock = getThreadLock(platformId);
//
//    synchronized (lock) {
//      logger.info(threadName + "开始同步数据");
    Long platformId = synchronizeModel.getPlatformId();
    Platform localData = findDataFromLocal(platformId, synchronizeModel.getSchemaId(), synchronizeModel.getTableId());
    Platform remoteData = findDataFromRemote(synchronizeModel.getUrl(), synchronizeModel.getUsername(), synchronizeModel.getPassword()
        , platformId, synchronizeModel.getSchemaName(), synchronizeModel.getTableName());
    Map<String, Set<Metadata>> discrepantData = mergeData(localData, remoteData);
    processingData(discrepantData);
//      logger.info(threadName + "结束同步数据");
//    }
//    //释放锁
//    releaseThreadLock(platformId);
  }

  protected abstract Platform findDataFromLocal(Long platformId, Long schemaId, Long tableId);

  protected abstract Platform findDataFromRemote(String url, String username, String password, Long platformId, String schemaName, String tableName);

  private Map<String, Set<Metadata>> mergeData(Platform localData, Platform remoteData) {
    return null;
  }

  private void processingData(Map<String, Set<Metadata>> discrepantData) {}

  private String getLockKey(Long platformId) {
    return "ETL_METADATA_EXTERNAL_PLATFORM_" + platformId;
  }

  private ThreadLock getThreadLock(Long platformId) throws InterruptedException {
    String threadName = Thread.currentThread().getName();
    ThreadLock lock;
    String lockKey = getLockKey(platformId);
    if(holderLock.tryLock(8, TimeUnit.SECONDS)) {
      logger.info(threadName + "获得ReentrantLock锁");
      try {
        Long end = System.currentTimeMillis() + 5000;
        do {
          lock = connectionHolder.get().get(lockKey);
        } while (null == lock && System.currentTimeMillis() - end < 0);
        lock = new ThreadLock();
        connectionHolder.get().put(lockKey, lock);
        logger.info(threadName + "获得ThreadLock锁");
        return lock;
      } finally {
        logger.info(threadName + "释放ReentrantLock锁");
        holderLock.unlock();
      }
    }
    throw new RequestTimeoutException("request timeout, please try again later");
  }

  private void releaseThreadLock(Long platformId) {
    String threadName = Thread.currentThread().getName();
    String lockKey = getLockKey(platformId);
    holderLock.lock();
    logger.info(threadName + "获得ReentrantLock锁");
    try {
      connectionHolder.get().remove(lockKey);
      logger.info(threadName + "释放ThreadLock锁");
    } finally {
      logger.info(threadName + "释放ReentrantLock锁");
      holderLock.unlock();
    }
  }
}
